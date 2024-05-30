package ru.otus.hw.repository;

import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List <Comment> findByBookId(Book book);

    Comment create(Comment comment);

    Comment update(Comment comment);

    void deleteById(long id);
}
