package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookCommentConverter;
import ru.otus.hw.services.BookCommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class BookCommentCommands {

    private final BookCommentConverter converter;

    private final BookCommentService service;

    /**
     * Найти все комментарии для книги
     */
    @ShellMethod(value = "Find all comments for book", key = "ac")
    public String findAllBookComments(long id) {
        return service.findByBookId(id).stream()
                .map(converter::bootCommentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    /**
     * Найти комментарий по id
     */
    @ShellMethod(value = "Find comments by id", key = "ic")
    public String findCommentById(long id) {
        return service.findById(id).stream()
                .map(converter::bootCommentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
