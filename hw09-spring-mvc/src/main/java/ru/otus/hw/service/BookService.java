package ru.otus.hw.service;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book findById(long id);

    List<Book> findAll();

    Book create(BookCreateDto dto);

    Book update(BookUpdateDto dto);

    void deleteById(long id);
}
