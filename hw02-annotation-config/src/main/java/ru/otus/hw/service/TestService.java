package ru.otus.hw.service;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

/**
 * API для для запуска тестирования конкретного студента.
 */
public interface TestService {
    TestResult executeTestFor(Student student);
}
