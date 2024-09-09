package ru.otus.hw.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        String msg = "Книги с данным ID нет в базе.";
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(msg));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book create(BookCreateDto dto) {
        var author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(dto.getAuthorId())));
        var genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(dto.getGenreId())));
        Book book = bookMapper.toBook(dto);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(BookUpdateDto dto) {
        String bookErrMsg = "Нет книги для обновления";
        Book book = bookRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException(bookErrMsg));
        book.setTitle(dto.getTitle());
        if (book.getAuthor().getId() != dto.getAuthorId()) {
            String authorErrMsg = "Не существует автора для обновления.";
            Author author = authorRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException(authorErrMsg));
            book.setAuthor(author);
        }

        if (book.getGenre().getId() != dto.getGenreId()) {
            String genreErrMsg = "Не существует жанра для обновления.";
            Genre genre = genreRepository.findById(dto.getGenreId())
                    .orElseThrow(() -> new EntityNotFoundException(genreErrMsg));
            book.setGenre(genre);
        }
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
