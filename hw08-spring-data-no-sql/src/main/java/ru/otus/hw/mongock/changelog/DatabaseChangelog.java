package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog
public class DatabaseChangelog {

    private final Map<String, Document> authors = new HashMap<>();

    private final Map<String, Document> genres = new HashMap<>();

    private final Map<String, Document> books = new HashMap<>();

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
        documents.forEach(document -> authors.put(document.getString("fullName"), document));
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
        documents.forEach(document -> genres.put(document.getString("name"), document));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "shidemere")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("books");
        List<Document> documents = List.of(
                new Document()
                        .append("title", "Автостопом по галактике")
                        .append("author", authors.get("Дуглас Адамс"))
                        .append("genre", genres.get("Научная фантастика")),

                new Document()
                        .append("title", "Идиот")
                        .append("author", authors.get("Фёдор Достоевский"))
                        .append("genre", genres.get("Роман")),

                new Document()
                        .append("title", "Биология добра и зла")
                        .append("author", authors.get("Роберт Сапольски"))
                        .append("genre", genres.get("Нон-фикшн"))
        );
        myCollection.insertMany(documents);
        documents.forEach(document -> books.put(document.getString("title"), document));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "shidemere")
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("comments");


        List<Document> documents = List.of(
                new Document().append("text", "Комментарий к книге 1")
                        .append("book", books.get("Автостопом по галактике")),
                new Document().append("text", "Комментарий к книге 2").append("book", books.get("Идиот")),
                new Document().append("text", "Комментарий к книге 3").append("book", books.get("Биология добра и зла"))
        );
        myCollection.insertMany(documents);
    }

}
