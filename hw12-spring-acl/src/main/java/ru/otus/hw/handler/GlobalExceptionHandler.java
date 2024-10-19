package ru.otus.hw.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.otus.hw.controller.rest.AuthorController;
import ru.otus.hw.controller.rest.BookController;
import ru.otus.hw.controller.rest.CommentController;
import ru.otus.hw.controller.rest.GenreController;
import ru.otus.hw.dto.ErrorMessage;

@RestControllerAdvice(basePackageClasses = {
        AuthorController.class,
        BookController.class,
        CommentController.class,
        GenreController.class}
)
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error("Entity not found exception: {}", ex.getMessage());
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Runtime exception: {}", ex.getMessage());
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGlobalException(Exception ex, WebRequest request) {
        log.error("General exception: ", ex);
        return new ErrorMessage(ex.getMessage());
    }
}
