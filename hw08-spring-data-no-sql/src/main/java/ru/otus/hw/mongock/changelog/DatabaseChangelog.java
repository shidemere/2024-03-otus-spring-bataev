package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jline.console.impl.Builtins;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;

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
        Author adams = Author.builder().fullName("Дуглас Адамс").build();
        Author dostoevskiy = Author.builder().fullName("Фёдор Достоевский").build();
        Author sapolskiy = Author.builder().fullName("Роберт Сапольски").build();

        List<Document> documents = List.of(
                new Document("Дуглас Адамс", adams),
                new Document("Фёдор Достоевский", dostoevskiy),
                new Document("Роберт Сапольски", sapolskiy)
        );
        myCollection.insertMany(documents);
        documents.forEach(document -> authors.put(document.getString("fullName"), document));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "shidemere")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("genres");
        Genre scifi = Genre.builder().name("Научная фантастика").build();
        Genre roman = Genre.builder().name("Роман").build();
        Genre nonFiction = Genre.builder().name("Нон-фикшн").build();
        List<Document> documents = List.of(
                new Document("Научная фантастика", scifi),
                new Document("Роман", roman),
                new Document("Нон-фикшн", nonFiction)
        );
        myCollection.insertMany(documents);
        documents.forEach(document -> genres.put(document.getString("name"), document));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "shidemere")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("books");
        Book guideTheGalaxy = Book.builder().title("Автостопом по галактике").build();
        Book idiot = Book.builder().title("Идиот").build();
        Book biologyOf = Book.builder().title("Биология добра и зла").build();
        List<Document> documents = List.of(
                new Document("Автостопом по галатике", guideTheGalaxy)
                        .append("author", authors.get("Дуглас Адамс"))
                        .append("genre", genres.get("Научная фантастика")),

                new Document("Идиот", idiot)
                        .append("author", authors.get("Фёдор Достоевский"))
                        .append("genre", genres.get("Роман")),

                new Document("Биология добра и зла", biologyOf)
                        .append("author", authors.get("Роберт Сапольски"))
                        .append("genre", genres.get("Нон-фикшн"))
        );
        myCollection.insertMany(documents);
        documents.forEach(document -> books.put(document.getString("title"), document));
    }

    @ChangeSet(order = "005", id = "insertComments", author = "shidemere")
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("comments");

        Comment first = Comment.builder().text("Комментарий к книге 1").build();
        Comment second = Comment.builder().text("Комментарий к книге 2").build();
        Comment third = Comment.builder().text("Комментарий к книге 3").build();

        List<Document> documents = List.of(
                new Document("Комментарий к книге 1", first).append("book", books.get("Автостопом по галактике")),
                new Document("Комментарий к книге 1", second).append("book", books.get("Идиот")),
                new Document("Комментарий к книге 3", third).append("book", books.get("Биология добра и зла"))
        );
        myCollection.insertMany(documents);
    }

}
