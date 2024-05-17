package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository {
    Optional<BookComment> findById(long id);

    List <BookComment> findByBook(Book book);

}
