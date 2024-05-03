package ru.otus.hw.config;

/**
 * Предоставляет параметр, для прохождения теста.
 * Считывается из application.yml
 */
public interface TestConfig {
    int getRightAnswersCountToPass();
}
