package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
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
    @Transactional
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    /**
     * Выборка всех книг. Делается 1 запросом.
     */
    @Override
    @Transactional
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Вставка нового пользователя. Делается 3 запросами.
     */
    @Override
    @Transactional
    public Book insert(String title, long authorId, long genreId) {
        return save(0, title, authorId, genreId);
    }

    /**
     * Обновление пользователя. Делается 4 запросами.
     */
    @Override
    @Transactional
    public Book update(long id, String title, long authorId, long genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Я не совсем понимаю следующее поведение.
     * Для инсерта происходит три запроса: селект автора, селект жанра, инсерт.
     * Для апдейта происходит четыре запроса: селекет автора, селект жанра, селект книги, апдейт.
     * Перед апдейтом нужно сделать селект, получается?
     */
    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = Book.builder()
                .id(id)
                .title(title)
                .author(author)
                .genre(genre)
                .build();
        return bookRepository.save(book);
    }
}
