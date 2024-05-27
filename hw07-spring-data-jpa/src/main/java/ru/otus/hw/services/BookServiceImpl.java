package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    /**
     * Получение книги по айдишнику за 1 запрос к бд.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    /**
     * Выборка всех книг. Делается 1 запросом.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Вставка нового пользователя. Делается 3 запросами.
     */
    @Override
    @Transactional
    public Book create(String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = Book.builder()
                .id(0)
                .title(title)
                .author(author)
                .genre(genre)
                .build();
        return bookRepository.save(book);
    }

    /**
     * Обновление пользователя. Делается 4 запросами.
     */
    @Override
    @Transactional
    public Book update(long id, String title, long authorId, long genreId) {
        String bookErrMsg = "Нет книги для обновления";
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(bookErrMsg));
        book.setTitle(title);
        if (book.getAuthor().getId() != authorId) {
            String authorErrMsg = "Не существует автора для обновления.";
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException(authorErrMsg));

            book.setAuthor(author);
        }

        if (book.getGenre().getId() != genreId) {
            String genreErrMsg = "Не существует жанра для обновления.";
            Genre genre = genreRepository.findById(authorId)
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
