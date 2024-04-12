package ru.otus.hw.service;

/**
 * API для работы с вводом-выводом.
 */
public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    // todo Как это можно применить?
    String readString();

    String readStringWithPrompt(String prompt);

    int readIntForRange(int min, int max, String errorMessage);

    // todo Как это можно применить?
    int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage);
}
