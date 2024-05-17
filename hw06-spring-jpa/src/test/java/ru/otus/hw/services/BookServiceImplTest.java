package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepositoryImpl;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты для {@link BookServiceImpl}
 */
@DisplayName("Проверка книг")
@DataJpaTest
@Import({
        BookServiceImpl.class,
        AuthorRepositoryImpl.class,
        GenreRepositoryImpl.class,
        BookRepositoryImpl.class
})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {


    @Autowired
    private BookServiceImpl service;

    private final long FIRST_BOOK_ID = 1L;

    private final long SECOND_BOOK_ID = 2L;

    private final long SIZE_OF_ALL_RECORD = 3;

    /**
     * Так как в ДЗ присутствует требование отключить транзакционность в тестовом классе
     * чтобы это не повлияло на транзакции в сервисах - тесты становятся зависимыми друг от друга
     * Поэтому на момент выполнение этого теста сущность с айдишником 1 может быть удалена.
     * Поэтому использвю другую сущность
     */
    @Test
    @DisplayName("Идентификатор сущности совпадает с ожидаемым")
    void findById_whenBookExist_thenIdMatch() {
        Optional<Book> actualBook = service.findById(SECOND_BOOK_ID);
        assertTrue(actualBook.isPresent());
        assertEquals(SECOND_BOOK_ID, actualBook.get().getId());
    }

    @Test
    @DisplayName("Все сущности находятся")
    void findAll_whenExistThreeRow_thenCorrectSize() {
        List<Book> all = service.findAll();
        assertEquals(all.size(), 3);
    }

    @Test
    @DisplayName("Сущность сохраняется")
    void insert_whenInsertNewEntity_thenSizeHasChanged() {
        service.insert("TestBook", 1L, 1L);
        List<Book> all = service.findAll();
        assertEquals(all.size(), SIZE_OF_ALL_RECORD + 1);
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


    // Тест проходит только если ни один комментарий не ссылается на книгу. Я не знаю как это исправить.
    @Test
    // @Rollback не помогает, так как его перебивает @Transactional сверху.
    @DisplayName("Сущность удаляется")
    void deleteById_whenEntityExist_thenCorrectDelete() {
        service.deleteById(FIRST_BOOK_ID);
        Optional<Book> book = service.findById(FIRST_BOOK_ID);
        assertTrue(book.isEmpty());
    }
}