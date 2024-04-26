package ru.otus.hw.service;

import ru.otus.hw.domain.Student;

/**
 * API для идентификации студента.
 */
public interface StudentService {

    Student determineCurrentStudent();
}
