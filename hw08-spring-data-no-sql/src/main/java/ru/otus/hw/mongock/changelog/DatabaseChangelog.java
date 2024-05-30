package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "shidemere", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "shidemere")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("authors");
        List<Document> documents = List.of(
                new Document().append("fullName", "Дуглас Адамс"),
                new Document().append("fullName", "Фёдор Достоевский"),
                new Document().append("fullName", "Роберт Сапольски")
        );
        myCollection.insertMany(documents);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "shidemere")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("genres");
        List<Document> documents = List.of(
                new Document().append("name", "Научная фантастика"),
                new Document().append("name", "Роман"),
                new Document().append("name", "Нон-фикшн")
        );
        myCollection.insertMany(documents);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "shidemere")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("books");
        List<Document> documents = List.of(
                new Document()
                        .append("title", "Автостопом по галактике")
                        .append("author", "Дуглас Адамс")
                        .append("genre", "Научная фантастика"),

                new Document()
                        .append("title", "Идиот")
                        .append("author", "Фёдор Достоевский")
                        .append("genre", "Роман"),

                new Document()
                        .append("title", "Биология добра и зла")
                        .append("author", "Роберт Сапольски")
                        .append("genre", "Нон-фикшн")
        );
        myCollection.insertMany(documents);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "shidemere")
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("comments");
        List<Document> books = List.of(
                new Document()
                        .append("title", "Автостопом по галактике")
                        .append("author", "Дуглас Адамс")
                        .append("genre", "Научная фантастика"),
                new Document()
                        .append("title", "Идиот")
                        .append("author", "Фёдор Достоевский")
                        .append("genre", "Роман"),
                new Document()
                        .append("title", "Биология добра и зла")
                        .append("author", "Роберт Сапольски")
                        .append("genre", "Нон-фикшн")
        );

        // todo Не понимаю как сделать через ссылки, а не встраиванием
        List<Document> documents = List.of(
                new Document().append("text", "Комментарий к книге 1").append("book", books.get(0)),
                new Document().append("text", "Комментарий к книге 2").append("book", books.get(1)),
                new Document().append("text", "Комментарий к книге 3").append("book", books.get(2))
        );
        myCollection.insertMany(documents);
    }

}
