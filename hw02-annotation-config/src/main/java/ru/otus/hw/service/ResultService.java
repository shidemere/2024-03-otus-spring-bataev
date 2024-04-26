package ru.otus.hw.service;

import ru.otus.hw.domain.TestResult;

/**
 * API для предоставления результатов тестирования.
 */
public interface ResultService {
    void showResult(TestResult testResult);
}
