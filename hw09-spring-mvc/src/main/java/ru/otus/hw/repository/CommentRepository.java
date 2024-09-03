package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(value = "book_entity_graph")
    List<Comment> findByBookId(long id);


}
