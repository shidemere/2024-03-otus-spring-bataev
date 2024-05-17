package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookCommentRepositoryImpl implements BookCommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public Optional<BookComment> findById(long id) {
        EntityGraph<?> graph = entityManager.getEntityGraph("book_entity_graph");
        Map<String, Object> properties = Map.of("jakarta.persistence.fetchgraph", graph);
        BookComment bookComment = entityManager.find(BookComment.class, id, properties);
        return Optional.ofNullable(bookComment);
    }


    @Override
    public List<BookComment> findByBook(Book book) {
        EntityGraph<?> graph = entityManager.getEntityGraph("book_entity_graph");

        TypedQuery<BookComment> query = entityManager.createQuery(
                "select c from BookComment c where c.book = :book", BookComment.class
        );
        query.setParameter("book", book);
        query.setHint("jakarta.persistence.fetchgraph", graph);
        return query.getResultList();
    }
}
