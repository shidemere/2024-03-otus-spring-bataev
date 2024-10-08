package ru.otus.hw.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;
import ru.otus.hw.service.security.GrantGivingService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GrantGivingService grantService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'READ')")
    public BookDto findById(long id) {
        String msg = String.format("Книги с данным {ID=%d} нет в базе.", id);
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(msg));
        return bookMapper.toBookDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(@bookMapperImpl.toBook(filterObject), 'READ')")
    public List<BookDto> findAll() {
        List<Book> all = bookRepository.findAll();
         return new ArrayList<>(all.stream()
                .map(bookMapper::toBookDto)
                .toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public BookDto create(BookCreateDto dto) {
        var author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(dto.getAuthorId())));
        var genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(dto.getGenreId())));
        Book book = bookMapper.toBook(dto);
        book.setAuthor(author);
        book.setGenre(genre);
        Book savedBook = bookRepository.save(book);
        //выдача прав
        grantService.setReadGrant(savedBook);
        grantService.setWriteGrant(savedBook);
        grantService.setCreateGrant(savedBook);
        grantService.setDeleteGrant(savedBook);
        grantService.setAdminGrant(savedBook);

        return bookMapper.toBookDto(savedBook);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(@bookMapperImpl.toBook(#dto), 'WRITE')")
    public BookDto update(BookUpdateDto dto) {
        String bookErrMsg = "Нет книги для обновления";
        Book book = bookRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException(bookErrMsg));
        book.setTitle(dto.getTitle());

        /* Тут не нужны проверки через IF потому что Hibernate не выполнит findById повторно если у него уже есть данные в кеше */
        String authorErrMsg = "Не существует автора для обновления.";
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException(authorErrMsg));
        book.setAuthor(author);

        String genreErrMsg = "Не существует жанра для обновления.";
        Genre genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException(genreErrMsg));
        book.setGenre(genre);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDto(savedBook);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'DELETE')")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
