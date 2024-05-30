package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converter.BookConverter;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    /**
     * Вывести все книги в консоль.
     */
    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    /**
     * Найти книгу по Id
     */
    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    /**
     * Создать книгу
     */
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorName, String genreText) {
        Author author = new Author(authorName);
        Genre genre = new Genre(genreText);
        var savedBook = bookService.create(title, author, genre);
        return bookConverter.bookToString(savedBook);
    }

    /**
     * Обновить существующую книгу
     */
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title,  String authorName,  String genreText) {
        Author author = new Author(authorName);
        Genre genre = new Genre(genreText);
        var savedBook = bookService.update(id, title, author, genre);
        return bookConverter.bookToString(savedBook);
    }

    /**
     * Удалить книгу
     */
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
