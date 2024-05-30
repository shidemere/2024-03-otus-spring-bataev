package ru.otus.hw.service;

import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(long id);

    List<Comment> findByBookId(long id);

    Comment update(long id, String text);

    Comment create(String text, long bookId);

    void deleteById(long id);
}
