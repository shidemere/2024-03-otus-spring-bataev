package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.model.Book;
import ru.otus.hw.repository.AuthorRepositoryImpl;
import ru.otus.hw.repository.BookRepositoryImpl;
import ru.otus.hw.repository.GenreRepositoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты для {@link BookServiceImpl}
 */
@DisplayName("Репозиторий для работы с книгами ")
@DataJpaTest
@Import({
        BookServiceImpl.class,
        AuthorRepositoryImpl.class,
        GenreRepositoryImpl.class,
        BookRepositoryImpl.class
})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class BookServiceImplTest {


    @Autowired
    private BookServiceImpl service;

    /**
     * Так как Lazy поля не участвуют в сравнении - можно не присваивать их
     */
    private final List<Book> ALL_BOOK_IN_DATABASE = Arrays.asList(
            new Book(1L, "BookTitle_1", null, null),
            new Book(2L, "BookTitle_2", null, null),
            new Book(3L, "BookTitle_3", null, null)
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
        Optional<Book> actualBook = service.findById(FIRST_BOOK_ID);
        assertTrue(actualBook.isPresent());
        assertEquals(FIRST_BOOK_ID, actualBook.get().getId());
    }

    @Test
    @DisplayName("Все сущности находятся")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAll_whenExistThreeRow_thenCorrectSize() {
        List<Book> all = service.findAll();


        assertIterableEquals(ALL_BOOK_IN_DATABASE, all);
    }

    @Test
    @DisplayName("Сущность сохраняется")
    void create_whenInsertNewEntity_thenSizeHasChanged() {
        Book inserted = service.create("TestBook", 1L, 1L);
        assertNotEquals(0, inserted.getId());
    }

    @Test
    @DisplayName("Сущность обновляется")
    void update_whenEntityUpdated_thenEntityHasChanged() {
        Optional<Book> oldBook = service.findById(FIRST_BOOK_ID);
        assertTrue(oldBook.isPresent());
        String oldName = oldBook.get().getTitle();
        service.update(FIRST_BOOK_ID, "First Title", 1L , 1L);
        Optional<Book> newBook = service.findById(FIRST_BOOK_ID);
        assertTrue(newBook.isPresent());
        assertNotEquals(newBook.get().getTitle(), oldName);
    }


    @Test
    @DisplayName("Сущность удаляется")
    void deleteById_whenEntityExist_thenCorrectDelete() {
        service.deleteById(FIRST_BOOK_ID);
        Optional<Book> book = service.findById(FIRST_BOOK_ID);
        assertTrue(book.isEmpty());
    }
}