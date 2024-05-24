package services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentServiceImpl;

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

    @Autowired
    private TestEntityManager testEntityManager;

    private final long FIRST_COMMENT_ID = 1L;

    private final long BOOK_ID_THAT_HAS_TWO_COMMENTS = 2L;

    @Test
    @DisplayName("Идентификатор сущности совпадает с ожидаемым")
    void findById_whenCommentExist_thenIdMatch() {
        Optional<Comment> comment = service.findById(FIRST_COMMENT_ID);
        assertTrue(comment.isPresent());
        assertEquals(FIRST_COMMENT_ID, comment.get().getId());
    }

    @Test
    @DisplayName("Получено ожидаемое количество записей с одинаковым внешним ключом")
    void findByBookId_whenFindByBook_thenCorrectReturn() {
        List<Comment> comments = service.findByBookId(BOOK_ID_THAT_HAS_TWO_COMMENTS);
        assertEquals(1, comments.size());
    }

    @Test
    @DisplayName("Сущность сохраняется")
    void create_whenSaveComment_thenReturnId() {
        Comment inserted = service.insert("text", 1);
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

    @Configuration
    @EnableJpaRepositories(basePackages = "ru.otus.hw.repositories")
    @EntityScan("ru.otus.hw.models")
    public static class JpaConfiguration {

    }
}
