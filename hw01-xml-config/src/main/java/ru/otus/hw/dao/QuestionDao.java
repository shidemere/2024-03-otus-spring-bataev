package ru.otus.hw.dao;

import ru.otus.hw.domain.Question;

import java.util.List;

/**
 * Предоставляет API для чтения их файла вопросов.
 */
public interface QuestionDao {
    List<Question> findAll();
}
