package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.model.mongo.DocumentAuthor;
import ru.otus.hw.model.mongo.DocumentBook;
import ru.otus.hw.model.mongo.Comment;
import ru.otus.hw.model.mongo.DocumentGenre;

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
        DocumentAuthor adams = DocumentAuthor.builder().fullName("Дуглас Адамс").build();
        DocumentAuthor dostoevskiy = DocumentAuthor.builder().fullName("Фёдор Достоевский").build();
        DocumentAuthor sapolskiy = DocumentAuthor.builder().fullName("Роберт Сапольски").build();
        template.save(adams);
        template.save(dostoevskiy);
        template.save(sapolskiy);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "shidemere")
    public void insertGenres(MongockTemplate template) {

        DocumentGenre scifi = DocumentGenre.builder().name("Научная фантастика").build();
        DocumentGenre roman = DocumentGenre.builder().name("Роман").build();
        DocumentGenre nonFiction = DocumentGenre.builder().name("Нон-фикшн").build();
        List<DocumentGenre> documents = List.of(
                scifi, roman, nonFiction
        );
        documents.forEach(template::save);

    }

    @ChangeSet(order = "004", id = "insertBooks", author = "shidemere")
    public void insertBooks(MongockTemplate template) {
        DocumentAuthor adams = template.findOne(
                Query.query(Criteria.where("fullName").is("Дуглас Адамс")), DocumentAuthor.class
        );
        DocumentAuthor dostoevskiy = template.findOne(
                Query.query(Criteria.where("fullName").is("Фёдор Достоевский")), DocumentAuthor.class
        );
        DocumentAuthor sapolskiy = template.findOne(
                Query.query(Criteria.where("fullName").is("Роберт Сапольски")), DocumentAuthor.class
        );

        // Находим жанры по имени
        DocumentGenre scifi = template.findOne(Query.query(Criteria.where("name").is("Научная фантастика")), DocumentGenre.class);
        DocumentGenre roman = template.findOne(Query.query(Criteria.where("name").is("Роман")), DocumentGenre.class);
        DocumentGenre nonFiction = template.findOne(Query.query(Criteria.where("name").is("Нон-фикшн")), DocumentGenre.class);

        List<DocumentBook> documents = List.of(
                DocumentBook.builder().title("Автостопом по галактике").documentAuthor(adams).documentGenre(scifi).build(),
                DocumentBook.builder().title("Идиот").documentAuthor(dostoevskiy).documentGenre(roman).build(),
                DocumentBook.builder().title("Биология добра и зла").documentAuthor(sapolskiy).documentGenre(nonFiction).build()
        );

        documents.forEach(template::save);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "shidemere")
    public void insertComments(MongockTemplate template) {

        DocumentBook idiot = template.findOne(
                Query.query(Criteria.where("title").is("Идиот")), DocumentBook.class
        );
        DocumentBook travelByGalactic = template.findOne(
                Query.query(Criteria.where("title").is("Автостопом по галактике")), DocumentBook.class
        );
        DocumentBook biologyOfEvilAndGood = template.findOne(
                Query.query(Criteria.where("title").is("Биология добра и зла")), DocumentBook.class
        );

        List<Comment> documents = List.of(
                Comment.builder().text("Идиот - хорошая книга").documentBook(idiot).build(),
                Comment.builder().text("Автостопом по галактике - хорошая книга").documentBook(travelByGalactic).build(),
                Comment.builder().text("Биология добра и зла - хорошая книга").documentBook(biologyOfEvilAndGood).build()
        );

        documents.forEach(template::save);
    }
}
