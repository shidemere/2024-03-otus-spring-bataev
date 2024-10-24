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
import ru.otus.hw.model.h2.EntityAuthor;
import ru.otus.hw.model.h2.EntityBook;
import ru.otus.hw.model.h2.EntityComment;
import ru.otus.hw.model.h2.EntityGenre;
import ru.otus.hw.model.mongo.DocumentAuthor;
import ru.otus.hw.model.mongo.DocumentBook;
import ru.otus.hw.model.mongo.Comment;
import ru.otus.hw.model.mongo.DocumentGenre;
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
    public MongoPagingItemReader<DocumentAuthor> authorReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<DocumentAuthor>()
                .name("mongoAuthorReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(DocumentAuthor.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<EntityAuthor> authorWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<EntityAuthor> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<DocumentAuthor, EntityAuthor> authorProcessor(AuthorService service) {

        return service::toSqlAuthor;
    }

    @Bean
    public Step transformAuthorsStep(
            ItemReader<DocumentAuthor> authorReader,
            JpaItemWriter<EntityAuthor> authorWriter,
            ItemProcessor<DocumentAuthor, EntityAuthor> authorProcessor,
            PlatformTransactionManager platformTransactionManager,
            TaskExecutor simpleTaskExecutor) {
        return new StepBuilder("transformAuthorsStep", jobRepository)
                .<DocumentAuthor, EntityAuthor>chunk(5, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .taskExecutor(simpleTaskExecutor)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения авторов");
                    }

                    public void afterRead(@NonNull DocumentAuthor documentAuthor) {
                        logger.info("Конец чтения для автора {}", documentAuthor.getFullName());
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения авторов");
                    }
                })
                .build();

    }

    /*
     * Работа с жанрами
     */

    @Bean
    public MongoPagingItemReader<DocumentGenre> genreReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<DocumentGenre>()
                .name("mongoGenreReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(DocumentGenre.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<EntityGenre> genreWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<EntityGenre> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<DocumentGenre, EntityGenre> genreProcessor(GenreService service) {
        return service::toSqlGenre;
    }

    @Bean
    public Step transformGenresStep(
            ItemReader<DocumentGenre> genreReader,
            JpaItemWriter<EntityGenre> genreWriter,
            ItemProcessor<DocumentGenre, EntityGenre> genreProcessor,
            PlatformTransactionManager platformTransactionManager,
            TaskExecutor simpleTaskExecutor) {
        return new StepBuilder("transforGenresStep", jobRepository)
                .<DocumentGenre, EntityGenre>chunk(5, platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .taskExecutor(simpleTaskExecutor)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения жанра");
                    }

                    public void afterRead(@NonNull DocumentGenre documentGenre) {
                        logger.info("Конец чтения для жанра {}", documentGenre.getName());
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения жанра");
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


    @Bean
    public Job importGenreAndAuthorJob(Flow splitFlow) {
        return new JobBuilder("GenreAndAuthorJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало async job для чтения жанров и авторов");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец async job для чтения жанров и авторов");
                    }
                })
                .build();
    }

    /*
     *  Работа с книгами
     */


    @Bean
    public MongoPagingItemReader<DocumentBook> bookReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<DocumentBook>()
                .name("mongoBookReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(DocumentBook.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<EntityBook> bookWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<EntityBook> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<DocumentBook, EntityBook> bookProcessor(BookService service) {
        return service::toSqlBook;
    }

    @Bean
    public Step transformBooksStep(
            ItemReader<DocumentBook> bookReader,
            JpaItemWriter<EntityBook> bookWriter,
            ItemProcessor<DocumentBook, EntityBook> bookProcessor,
            PlatformTransactionManager platformTransactionManager
            ) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<DocumentBook, EntityBook>chunk(5, platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения книг");
                    }

                    public void afterRead(@NonNull DocumentBook documentBook) {
                        logger.info("Конец чтения для книги {}", documentBook.getTitle());
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
    public JpaItemWriter<EntityComment> commentWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<EntityComment> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public ItemProcessor<Comment, EntityComment> sqlCommentProcessor(CommentService service) {
        return service::toSqlComment;
    }

    @Bean
    public Step transformCommentsStep(
            ItemReader<Comment> commentReader,
            JpaItemWriter<EntityComment> commentWriter,
            ItemProcessor<Comment, EntityComment> commentProcessor,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("transformCommentsStep", jobRepository)
                .<Comment, EntityComment>chunk(5, platformTransactionManager)
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
