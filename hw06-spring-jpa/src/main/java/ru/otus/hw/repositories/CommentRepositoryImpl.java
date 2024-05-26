package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
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
    public List<Comment> findByBookId(Book book) {
        String sql = "SELECT c FROM Comment c WHERE book = :book_id";
        TypedQuery<Comment> query = entityManager.createQuery(sql, Comment.class);
        query.setParameter("book_id", book);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        Optional<Comment> comment = Optional.ofNullable(entityManager.find(Comment.class, id));
        entityManager.remove(comment.orElse(null));
    }


    @Override
    public Comment create(Comment comment) {
        String errMsg = "Комментарий не может быть привязан к книге";
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        return entityManager.merge(comment);
    }

}
