package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        Comment comment = entityManager.find(Comment.class, id);
        return Optional.ofNullable(comment);
    }


    @Override
    public List<Comment> findByBookId(long bookId) {
        EntityGraph<?> genreAuthorEntityGraph = entityManager.getEntityGraph("genre_author_comment_entity_graph");
        Book book = entityManager.find(
                Book.class, bookId, Map.of("jakarta.persistence.fetchgraph", genreAuthorEntityGraph)
        );
        String errMsg = "Не найдена книга с таким ID для получения комментариев.";
        return Optional.ofNullable(book).orElseThrow(() -> new EntityNotFoundException(errMsg)).getComments();
    }

    @Override
    public void deleteById(long id) {
        Optional<Comment> comment = Optional.ofNullable(entityManager.find(Comment.class, id));
        String errMsg = "Нет комментария для удаления";
        entityManager.remove(comment.orElseThrow(() -> new EntityNotFoundException(errMsg)));


    }


    @Override
    public Comment insert(Comment comment, long bookId) {
        EntityGraph<?> genreAuthorEntityGraph = entityManager.getEntityGraph("genre_author_comment_entity_graph");
        Optional<Book> book = Optional.ofNullable(entityManager.find(
                Book.class, bookId, Map.of("jakarta.persistence.fetchgraph", genreAuthorEntityGraph)
        ));
        String errMsg = "Комментарий не может быть привязан к книге";
        comment.setBook(book.orElseThrow(() -> new EntityNotFoundException(errMsg)));
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        return entityManager.merge(comment);
    }

}
