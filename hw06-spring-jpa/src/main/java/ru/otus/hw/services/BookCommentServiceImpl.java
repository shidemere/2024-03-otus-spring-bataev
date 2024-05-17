package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {

    private final BookCommentRepository bookCommentRepository;

    private final BookRepository bookRepository;

    /**
     * Делается в 1 запрос.
     */
    @Override
    @Transactional
    public Optional<BookComment> findById(long id) {
        return bookCommentRepository.findById(id);
    }

    /**
     * Делается в 2 запроса. Как уместить в 1?
     */
    @Override
    @Transactional
    public List<BookComment> findByBookId(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return bookCommentRepository.findByBook(book.orElse(new Book()));
    }
}
