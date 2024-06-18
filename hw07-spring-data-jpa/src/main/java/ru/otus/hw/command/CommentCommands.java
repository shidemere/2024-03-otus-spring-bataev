package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converter.CommentConverter;
import ru.otus.hw.model.Comment;
import ru.otus.hw.service.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentConverter converter;

    private final CommentService service;

    /**
     * Найти все комментарии для книги
     */
    // todo Сессия закрывается, исправь
    @ShellMethod(value = "Find all comments for book", key = "ac")
    public String findAllBookComments(long id) {
        return service.findByBookId(id).stream()
                .map(converter::bootCommentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    /**
     * Найти комментарий по id
     */
    // todo Сессия закрывается, исправь
    @ShellMethod(value = "Find comments by id", key = "ic")
    public String findCommentById(long id) {
        return service.findById(id).stream()
                .map(converter::bootCommentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }


    @ShellMethod(value = "Insert Comment", key = "sv")
    public String saveComment(String text, long bookId) {
        Comment updated = service.create(text, bookId);
        return updated.getText();
    }


}
