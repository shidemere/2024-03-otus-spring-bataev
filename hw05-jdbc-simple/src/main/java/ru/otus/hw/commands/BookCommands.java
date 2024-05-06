package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    /**
     * Вывести список всех книг в консоль
     */
    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    /**
     * Найти книгу по Id
     * @param id идентификатор кни
     * Пример запроса: bbid 1
     */
    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    /**
     * Добавить новую книгу
     * @param title название книги
     * @param authorId автор книги
     * @param genreId жанр книги
     * Пример запроса: "Записки примата" 3 3
     */
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, long genreId) {
        var savedBook = bookService.insert(title, authorId, genreId);
        return bookConverter.bookToString(savedBook);
    }

    /**
     * Обновить существующую книгу
     * @param id идентификатор книги
     * @param title название книги
     * @param authorId автор книги
     * @param genreId жанр книги
     * Пример запроса: bupd 1 "42" 1 1
     */
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, long genreId) {
        var savedBook = bookService.update(id, title, authorId, genreId);
        return bookConverter.bookToString(savedBook);
    }

    /**
     * Удаление книги
     * @param id идентификатор книги
     * Пример запроса: bdel 2
     */
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
