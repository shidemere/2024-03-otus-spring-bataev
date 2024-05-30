package ru.otus.hw.service;

import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(String id);

    List<Comment> findByBookId(String id);

    Comment update(String id, String text);

    Comment create(String text, Book book);

    void deleteById(String id);
}
