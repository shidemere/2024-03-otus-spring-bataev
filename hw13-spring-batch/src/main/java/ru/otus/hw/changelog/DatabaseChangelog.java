package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.model.mongo.Author;
import ru.otus.hw.model.mongo.Book;
import ru.otus.hw.model.mongo.Comment;
import ru.otus.hw.model.mongo.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog(order = "001")
public class DatabaseChangelog {

    private final Map<String, Document> authors = new HashMap<>();

    @ChangeSet(order = "001", id = "dropDb", author = "shidemere", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "shidemere")
    public void insertAuthors(MongockTemplate template) {
        Author adams = Author.builder().fullName("Дуглас Адамс").build();
        Author dostoevskiy = Author.builder().fullName("Фёдор Достоевский").build();
        Author sapolskiy = Author.builder().fullName("Роберт Сапольски").build();
        template.save(adams);
        template.save(dostoevskiy);
        template.save(sapolskiy);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "shidemere")
    public void insertGenres(MongockTemplate template) {

        Genre scifi = Genre.builder().name("Научная фантастика").build();
        Genre roman = Genre.builder().name("Роман").build();
        Genre nonFiction = Genre.builder().name("Нон-фикшн").build();
        List<Genre> documents = List.of(
                scifi, roman, nonFiction
        );
        documents.forEach(template::save);

    }

    @ChangeSet(order = "004", id = "insertBooks", author = "shidemere")
    public void insertBooks(MongockTemplate template) {

        Author adams = template.findOne(Query.query(Criteria.where("fullName").is("Дуглас Адамс")), Author.class);
        Author dostoevskiy = template.findOne(Query.query(Criteria.where("fullName").is("Фёдор Достоевский")), Author.class);
        Author sapolskiy = template.findOne(Query.query(Criteria.where("fullName").is("Роберт Сапольски")), Author.class);

        // Находим жанры по имени
        Genre scifi = template.findOne(Query.query(Criteria.where("name").is("Научная фантастика")), Genre.class);
        Genre roman = template.findOne(Query.query(Criteria.where("name").is("Роман")), Genre.class);
        Genre nonFiction = template.findOne(Query.query(Criteria.where("name").is("Нон-фикшн")), Genre.class);

        List<Book> documents = List.of(
                Book.builder()
                        .title("Автостопом по галактике")
                        .author(adams)
                        .genre(scifi)
                        .build(),
                Book.builder()
                        .title("Идиот")
                        .author(dostoevskiy)
                        .genre(roman)
                        .build(),
                Book.builder()
                        .title("Биология добра и зла")
                        .author(sapolskiy)
                        .genre(nonFiction)
                        .build()
        );

        documents.forEach(template::save);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "shidemere")
    public void insertComments(MongockTemplate template) {

        Book idiot = template.findOne(Query.query(Criteria.where("title").is("Идиот")), Book.class);
        Book travelByGalactic = template.findOne(Query.query(Criteria.where("title").is("Автостопом по галактике")), Book.class);
        Book biologyOfEvilAndGood = template.findOne(Query.query(Criteria.where("title").is("Биология добра и зла")), Book.class);

        List<Comment> documents = List.of(
                Comment.builder()
                        .text("Идиот - хорошая книга")
                        .book(idiot)
                        .build(),
                Comment.builder()
                        .text("Автостопом по галактике - хорошая книга")
                        .book(travelByGalactic)
                        .build(),
                Comment.builder()
                        .text("Биология добра и зла - хорошая книга")
                        .book(biologyOfEvilAndGood)
                        .build()
        );

        documents.forEach(template::save);
    }
}
