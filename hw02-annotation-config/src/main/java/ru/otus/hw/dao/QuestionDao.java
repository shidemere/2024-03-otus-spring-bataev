package ru.otus.hw.dao;

import ru.otus.hw.domain.Question;

import java.util.List;

/**
 * API для получения вопросов из ресурса.
 */
public interface QuestionDao {
    List<Question> findAll();
}
