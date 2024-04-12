package ru.otus.hw.config;

/**
 * API для установки минимально-проходного балла при тестировании.
 */
public interface TestConfig {
    int getRightAnswersCountToPass();
}
