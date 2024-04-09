package ru.otus.hw.exceptions;

// TODO Я без понятия где можно это применять, мой код не выбрасывает проверяемых исключений.
public class QuestionReadException extends RuntimeException {
    public QuestionReadException(String message, Throwable ex) {
        super(message, ex);
    }
}
