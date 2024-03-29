package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object... args);

    /**
     * Так как с пользователем ничего делать не надо - возвращаемое значение пустое.
     *
     * @param name строка для приветствия.
     */
    void requireUserInfo(String name);

    void prettyOutput(List<Question> list);
}
