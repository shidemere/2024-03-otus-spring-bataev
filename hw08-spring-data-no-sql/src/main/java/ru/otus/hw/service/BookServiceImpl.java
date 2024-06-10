package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }


    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(String title, String authorId, String genreId) {
        String errMsgAuthor = "Не найден автор для создания книги";
        String errMsgGenre = "Не найден жанр для создания книги";
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException(errMsgAuthor));
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException(errMsgGenre));
        var book = Book.builder()
                .title(title)
                .author(author)
                .genre(genre)
                .build();
        return bookRepository.save(book);
    }

    @Override
    public Book update(String id, String title, String authorId, String genreId) {
        String errMsgAuthor = "Не найден автор для создания книги";
        String errMsgGenre = "Не найден жанр для создания книги";
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException(errMsgAuthor));
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException(errMsgGenre));
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
