package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }


    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(String title, Author author, Genre genre) {
        var book = Book.builder()
                .title(title)
                .author(author)
                .genre(genre)
                .build();
        return bookRepository.save(book);
    }

    @Override
    public Book update(String id, String title, Author author, Genre genre) {
        String bookErrMsg = "Нет книги для обновления";
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(bookErrMsg));
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);

    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}
