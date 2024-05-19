package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> genreAuthorEntityGraph = entityManager.getEntityGraph("genre_author_comment_entity_graph");
        Book book = entityManager.find(Book.class, id, Map.of("jakarta.persistence.fetchgraph", genreAuthorEntityGraph));
        return Optional.ofNullable(book);
    }



    @Override
    public List<Book> findAll() {
        EntityGraph<?> genreAuthorEntityGraph = entityManager.getEntityGraph("genre_author_comment_entity_graph");
        TypedQuery<Book> getAllBook = entityManager.createQuery("select b from Book b", Book.class);

        getAllBook.setHint("jakarta.persistence.fetchgraph", genreAuthorEntityGraph);
        return getAllBook.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        EntityGraph<?> genreAuthorEntityGraph = entityManager.getEntityGraph("genre_author_comment_entity_graph");
        Book book = entityManager.find(Book.class, id, Map.of("jakarta.persistence.fetchgraph", genreAuthorEntityGraph));
        if (Objects.isNull(book)) {
            return;
        }

        entityManager.remove(book);
    }

    private Book insert(Book book) {
        entityManager.persist(book);
        return book;
    }

    private Book update(Book book) {
        return entityManager.merge(book);
    }


}
