package ru.otus.hw.service;

import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(String id);

    List<Book> findAll();

    Book create(String title, String authorId, String genreId);

    Book update(String id, String title, String authorId, String genreId);

    void deleteById(String id);
}
