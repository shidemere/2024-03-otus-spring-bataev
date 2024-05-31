package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий для работы с книгами ")
@DataJpaTest
@Import({
        BookServiceImpl.class,
        CommentServiceImpl.class
})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BookServiceImplTest {


    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private CommentServiceImpl commentService;

    /**
     * Так как Lazy поля не участвуют в сравнении - можно не присваивать их
     * UPD но ментор сказал сделать
     */
    private final List<Book> ALL_BOOK_IN_DATABASE = Arrays.asList(
            new Book(1L, "BookTitle_1", new Author(1, "Author_1"), new Genre(1, "Genre_1")),
            new Book(2L, "BookTitle_2", new Author(2, "Author_2"), new Genre(2, "Genre_2") ),
            new Book(3L, "BookTitle_3", new Author(3, "Author_3"), new Genre(3, "Genre_3"))
    );
    private final long FIRST_BOOK_ID = 1L;


    /**
     * Так как в ДЗ присутствует требование отключить транзакционность в тестовом классе
     * чтобы это не повлияло на транзакции в сервисах - тесты становятся зависимыми друг от друга
     * Поэтому на момент выполнение этого теста сущность с айдишником 1 может быть удалена.
     * Поэтому использвю другую сущность
     */
    @Test
    @DisplayName("Идентификатор сущности совпадает с ожидаемым")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findById_whenBookExist_thenIdMatch() {
        Optional<Book> actualBook = bookService.findById(FIRST_BOOK_ID);
        assertTrue(actualBook.isPresent());
        assertEquals(ALL_BOOK_IN_DATABASE.get(0), actualBook.get());
    }

    @Test
    @DisplayName("Все сущности находятся")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAll_whenExistThreeRow_thenCorrectSize() {
        List<Book> all = bookService.findAll();
        assertIterableEquals(ALL_BOOK_IN_DATABASE, all);
    }

    @Test
    @DisplayName("Сущность сохраняется")
    void create_whenInsertNewEntity_thenSizeHasChanged() {
        Book inserted = bookService.create("TestBook", 1L, 1L);
        assertNotEquals(0, inserted.getId());
    }

    @Test
    @DisplayName("Сущность обновляется")
    void update_whenEntityUpdated_thenEntityHasChanged() {
        Optional<Book> oldBook = bookService.findById(FIRST_BOOK_ID);
        assertTrue(oldBook.isPresent());
        String oldName = oldBook.get().getTitle();
        bookService.update(FIRST_BOOK_ID, "First Title", 1L , 1L);
        Optional<Book> newBook = bookService.findById(FIRST_BOOK_ID);
        assertTrue(newBook.isPresent());
        assertNotEquals(newBook.get().getTitle(), oldName);
    }


    @Test
    @DisplayName("Сущность удаляется")
    void deleteById_whenEntityExist_thenCorrectDelete() {
        bookService.deleteById(FIRST_BOOK_ID);
        Optional<Book> book = bookService.findById(FIRST_BOOK_ID);
        List<Comment> comments = commentService.findByBookId(FIRST_BOOK_ID);
        assertTrue(book.isEmpty());
        assertTrue(comments.isEmpty());
    }

}
