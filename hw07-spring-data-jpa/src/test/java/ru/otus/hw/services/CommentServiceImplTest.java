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

@DisplayName("Проверка комментариев к книге")
@DataJpaTest
@Import({
        CommentServiceImpl.class,
})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CommentServiceImplTest {
    @Autowired
    private CommentServiceImpl service;

    private final long FIRST_COMMENT_ID = 1L;

    private final long FIRST_BOOK_ID = 1L;

    private final List<Book> ALL_BOOK_IN_DATABASE = Arrays.asList(
            new Book(1L, "BookTitle_1", new Author(1, "Author_1"), new Genre(1, "Genre_1")),
            new Book(2L, "BookTitle_2", new Author(2, "Author_2"), new Genre(2, "Genre_2")),
            new Book(3L, "BookTitle_3", new Author(3, "Author_3"), new Genre(3, "Genre_3"))
    );

    private final List<Comment> ALL_COMMENTS_IN_DATABASE = Arrays.asList(
            new Comment(1L, "Comment 1", ALL_BOOK_IN_DATABASE.get(0)),
            new Comment(2L, "Comment 2", ALL_BOOK_IN_DATABASE.get(1)),
            new Comment(3L, "Comment 3", ALL_BOOK_IN_DATABASE.get(2))
    );

    @Test
    @DisplayName("Идентификатор сущности совпадает с ожидаемым")
    void findById_whenCommentExist_thenEquals() {
        Optional<Comment> comment = service.findById(FIRST_COMMENT_ID);
        assertTrue(comment.isPresent());
        assertEquals(ALL_COMMENTS_IN_DATABASE.get(0), comment.get());
    }

    @Test
    @DisplayName("Получено ожидаемое количество записей с одинаковым внешним ключом")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findByBookId_whenFindByBook_thenCorrectReturn() {
        List<Comment> comments = service.findByBookId(FIRST_BOOK_ID);
        assertEquals(1, comments.size());
        // из за индексации с нуля в листе - отнимаем единицу
        assertEquals(ALL_BOOK_IN_DATABASE.get((int) FIRST_BOOK_ID - 1), comments.get(0).getBook());
    }

    @Test
    @DisplayName("Сущность сохраняется")
    void create_whenSaveComment_thenReturnId() {
        Comment inserted = service.create("text", 1);
        System.out.println(inserted);
        assertNotEquals(0, inserted.getId());
    }

    @Test
    @DisplayName("Сущность обновляется")
    void update_whenUpdateComment_thenReturnUpdatedComment() {
        Optional<Comment> optional = service.findById(FIRST_COMMENT_ID);
        assertTrue(optional.isPresent());
        String oldText = optional.get().getText();

        service.update(optional.get().getId(), "NEW TEXT");

        Optional<Comment> updated = service.findById(FIRST_COMMENT_ID);
        assertTrue(updated.isPresent());
        assertNotEquals(updated.get().getText(), oldText);
    }

    @Test
    @DisplayName("Сущность удаляется")
    void deleteByIf_whenUpdateComment_thenNoneInTable() {
        service.deleteById(FIRST_COMMENT_ID);
        Optional<Comment> comment = service.findById(FIRST_COMMENT_ID);
        assertTrue(comment.isEmpty());
    }


}
