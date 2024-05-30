package ru.otus.hw.service;

import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(String id);

    List<Book> findAll();

    Book create(String title, Author author, Genre genre);

    Book update(String id, String title, Author author, Genre genre);

    void deleteById(String id);
}
