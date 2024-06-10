package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import({
        BookServiceImpl.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class
})
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private GenreServiceImpl genreService;
    @Autowired
    private AuthorServiceImpl authorService;


    @Test
    void findById() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");

        Book book = bookService.create("Граф Монте-Кристо", author.getId(), genre.getId());
        assertNotNull(book.getId());

        Optional<Book> byId = bookService.findById(book.getId());
        assertTrue(byId.isPresent());
    }

    @Test
    void findAll() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");
        List<Book> books = List.of(
                bookService.create("Граф Монте-Кристо", author.getId(), genre.getId()),
                bookService.create("Две Дианы", author.getId(), genre.getId())
        );

        books.stream()
                .map(Book::getId)
                .forEach(Assertions::assertNotNull);
    }

    @Test
    void create() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");

        Book book = bookService.create("Граф Монте-Кристо", author.getId(), genre.getId());
        assertNotNull(book.getId());
    }

    @Test
    void update() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");

        Book book = bookService.create("Граф Монте-Кристо", author.getId(), genre.getId());
        assertNotNull(book.getId());

        Book updated = bookService.update(book.getId(), "Две Дианы", book.getAuthor().getId(), book.getGenre().getId());
        assertEquals(updated.getTitle(), "Две Дианы");
    }

    @Test
    void deleteById() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");

        Book book = bookService.create("Граф Монте-Кристо", author.getId(), genre.getId());
        assertNotNull(book.getId());

        Optional<Book> byId = bookService.findById(book.getId());
        assertTrue(byId.isPresent());

        bookService.deleteById(byId.get().getId());
        Optional<Book> byIdAfterDeleting = bookService.findById(book.getId());
        assertTrue(byIdAfterDeleting.isEmpty());
    }
}