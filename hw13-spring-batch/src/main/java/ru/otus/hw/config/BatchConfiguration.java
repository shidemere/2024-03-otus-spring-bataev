package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.h2.SQLAuthor;
import ru.otus.hw.model.h2.SQLBook;
import ru.otus.hw.model.h2.SQLComment;
import ru.otus.hw.model.h2.SQLGenre;
import ru.otus.hw.model.mongo.Author;
import ru.otus.hw.model.mongo.Book;
import ru.otus.hw.model.mongo.Comment;
import ru.otus.hw.model.mongo.Genre;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.service.DatabaseCleanupService;
import ru.otus.hw.service.GenreService;

import java.util.HashMap;

@Configuration
public class BatchConfiguration {

    /*
     *  Общие настройки
     */

    private final Logger logger = LoggerFactory.getLogger("Config Logger");

    @Autowired
    private JobRepository jobRepository;

    @Bean
    public TaskExecutor simpleTaskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    /*
     * Работа с авторами
     */

    @Bean
    public MongoPagingItemReader<Author> authorReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Author>()
                .name("mongoAuthorReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Author.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<SQLAuthor> authorWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<SQLAuthor> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<Author, SQLAuthor> authorProcessor(AuthorService service) {

        return service::toSqlAuthor;
    }

    @Bean
    public Step transformAuthorsStep(
            ItemReader<Author> authorReader,
            JpaItemWriter<SQLAuthor> authorWriter,
            ItemProcessor<Author, SQLAuthor> authorProcessor,
            PlatformTransactionManager platformTransactionManager,
            TaskExecutor simpleTaskExecutor) {
        return new StepBuilder("transformAuthorsStep", jobRepository)
                .<Author, SQLAuthor>chunk(5, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .taskExecutor(simpleTaskExecutor)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения авторов");
                    }

                    public void afterRead(@NonNull Author author) {
                        logger.info("Конец чтения для автора {}", author.getFullName());
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения авторов");
                    }
                })
                .build();

    }

    @Bean
    public Job importAuthorJob(Step transformAuthorsStep) {
        return new JobBuilder("AuthorJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformAuthorsStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job для авторов");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job для авторов");
                    }
                })
                .build();
    }


    /*
     * Работа с жанрами
     */

    @Bean
    public MongoPagingItemReader<Genre> genreReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Genre>()
                .name("mongoGenreReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Genre.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<SQLGenre> genreWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<SQLGenre> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<Genre, SQLGenre> genreProcessor(GenreService service) {
        return service::toSqlGenre;
    }

    @Bean
    public Step transformGenresStep(
            ItemReader<Genre> genreReader,
            JpaItemWriter<SQLGenre> genreWriter,
            ItemProcessor<Genre, SQLGenre> genreProcessor,
            PlatformTransactionManager platformTransactionManager,
            TaskExecutor simpleTaskExecutor) {
        return new StepBuilder("transforGenresStep", jobRepository)
                .<Genre, SQLGenre>chunk(5, platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .taskExecutor(simpleTaskExecutor)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения жанра");
                    }

                    public void afterRead(@NonNull Genre genre) {
                        logger.info("Конец чтения для жанра {}", genre.getName());
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения жанра");
                    }
                })
                .build();

    }

    @Bean
    public Job importGenreJob(Step transformGenresStep) {
        return new JobBuilder("GenreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformGenresStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job для чтения жанров");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job для чтения жанров");
                    }
                })
                .build();
    }

    /*
     * Настройка паралельного запуска для жанров и авторов
     */

    @Bean
    public Flow splitFlow(TaskExecutor simpleTaskExecutor, Flow asyncAuthorFlow, Flow asyncGenreFlow) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(simpleTaskExecutor)
                .add(asyncAuthorFlow, asyncGenreFlow)
                .build();
    }

    @Bean
    public Flow asyncAuthorFlow(Step transformAuthorsStep) {
        return new FlowBuilder<SimpleFlow>("asyncAuthorFlow")
                .start(transformAuthorsStep)
                .build();
    }

    @Bean
    public Flow asyncGenreFlow(Step transformGenresStep) {
        return new FlowBuilder<SimpleFlow>("asyncGenreFlow")
                .start(transformGenresStep)
                .build();
    }


    /*
     *  Работа с книгами
     */


    @Bean
    public MongoPagingItemReader<Book> bookReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Book>()
                .name("mongoBookReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Book.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<SQLBook> bookWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<SQLBook> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<Book, SQLBook> bookProcessor(BookService service) {
        return service::toSqlBook;
    }

    @Bean
    public Step transformBooksStep(
            ItemReader<Book> bookReader,
            JpaItemWriter<SQLBook> bookWriter,
            ItemProcessor<Book, SQLBook> bookProcessor,
            PlatformTransactionManager platformTransactionManager
            ) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<Book, SQLBook>chunk(5, platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения книг");
                    }

                    public void afterRead(@NonNull Book book) {
                        logger.info("Конец чтения для книги {}", book.getTitle());
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения книг");
                    }
                })
                .build();

    }

    @Bean
    public Job importBookJob(Step transformBooksStep) {
        return new JobBuilder("BookJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformBooksStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job для чтения книг");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job для чтения книг");
                    }
                })
                .build();
    }

    /*
     *  Работа с комментариями.
     */



    @Bean
    public MongoPagingItemReader<Comment> commentReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<Comment>()
                .name("mongoCommentReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Comment.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<SQLComment> commentWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<SQLComment> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<Comment, SQLComment> sqlCommentProcessor(CommentService service) {
        return service::toSqlComment;
    }

    @Bean
    public Step transformCommentsStep(
            ItemReader<Comment> commentReader,
            JpaItemWriter<SQLComment> commentWriter,
            ItemProcessor<Comment, SQLComment> commentProcessor,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("transformCommentsStep", jobRepository)
                .<Comment, SQLComment>chunk(5, platformTransactionManager)
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения комментариев");
                    }

                    public void afterRead(@NonNull Comment comment) {
                        logger.info("Конец чтения для комментария {}", comment.getText());
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения комментариев");
                    }
                })
                .build();

    }

    @Bean
    public Job importCommentJob(Step transformCommentsStep) {
        return new JobBuilder("CommentJon", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformCommentsStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job для чтения комментариев");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job для чтения комментариев");
                    }
                })
                .build();
    }

    /*
     *  Функционал для рестарта задачи
     */

    @Bean
    public MethodInvokingTaskletAdapter cleanupTasklet(DatabaseCleanupService databaseCleanupService) {
        MethodInvokingTaskletAdapter tasklet = new MethodInvokingTaskletAdapter();
        tasklet.setTargetObject(databaseCleanupService);
        tasklet.setTargetMethod("cleanDatabase");
        return tasklet;
    }

    @Bean
    public Step cleanupStep(
            MethodInvokingTaskletAdapter cleanupTasklet,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet(cleanupTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Job clearDatabaseJob(Step cleanupStep) {
        return new JobBuilder("clearDatabaseJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(cleanupStep)
                .end()
                .build();
    }
}
