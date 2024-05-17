package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.AuthorRepositoryImpl;
import ru.otus.hw.repositories.BookCommentRepositoryImpl;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Проверка комментариев к книге")
@DataJpaTest
@Import({
        BookCommentServiceImpl.class,
        BookCommentRepositoryImpl.class,
        AuthorRepositoryImpl.class,
        GenreRepositoryImpl.class,
        BookRepositoryImpl.class
})
@Transactional(propagation = Propagation.NEVER)
class BookCommentServiceImplTest {

    @Autowired
    private BookCommentServiceImpl service;

    private final long FIRST_COMMENT_ID = 1L;

    private final long BOOK_ID_THAT_HAS_TWO_COMMENTS = 2L;

    @Test
    @DisplayName("Идентификатор сущности совпадает с ожидаемым")
    void findById_whenCommentExist_thenIdMatch() {
        Optional<BookComment> comment = service.findById(FIRST_COMMENT_ID);
        assertTrue(comment.isPresent());
        assertEquals(FIRST_COMMENT_ID, comment.get().getId());
    }

    @Test
    @DisplayName("Получено ожидаемое количество записей с одинаковым внешним ключом")
    void findByBookId_whenFindByBook_thenCorrectReturn() {
        List<BookComment> comments = service.findByBookId(BOOK_ID_THAT_HAS_TWO_COMMENTS);
        assertEquals(2, comments.size());
    }
}