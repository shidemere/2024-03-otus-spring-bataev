package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import({
        BookServiceImpl.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class,
        CommentServiceImpl.class
})
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CommentServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;
    @Autowired
    private GenreServiceImpl genreService;
    @Autowired
    private AuthorServiceImpl authorService;
    @Autowired
    private CommentServiceImpl commentService;


    @Test
    void findById() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");
        Book book = bookService.create("This is a title", author.getId(), genre.getId());
        Comment comment = commentService.create("This is a comment", book.getId());

        assertNotNull(comment.getId());

        Optional<Comment> byId = commentService.findById(comment.getId());
        assertTrue(byId.isPresent());
    }

    @Test
    void findByBookId() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");
        Book book = bookService.create("This is a title", author.getId(), genre.getId());
        Comment comment = commentService.create("This is a comment", book.getId());

        assertNotNull(comment.getId());
        List<Comment> comments = commentService.findByBookId(book.getId());
        assertEquals(1, comments.size());
        assertEquals(comments.get(0).getText(), comment.getText());

    }

    @Test
    void update() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");
        Book book = bookService.create("This is a title", author.getId(), genre.getId());
        Comment comment = commentService.create("This is an old comment", book.getId());

        assertNotNull(comment.getId());

        Comment updated = commentService.update(comment.getId(), "This is a new comment");
        assertEquals("This is a new comment", updated.getText());
    }

    @Test
    void create() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");
        Book book = bookService.create("This is a title", author.getId(), genre.getId());
        Comment comment = commentService.create("This is a comment", book.getId());

        assertNotNull(comment.getId());
    }

    @Test
    void deleteById() {
        Author author = authorService.create("Дюма");
        Genre genre = genreService.create("Роман");
        Book book = bookService.create("This is a title", author.getId(), genre.getId());
        Comment comment = commentService.create("This is a comment", book.getId());

        assertNotNull(comment.getId());

        commentService.deleteById(comment.getId());
        assertTrue(commentService.findById(comment.getId()).isEmpty());
    }
}