package ru.otus.hw.service;

/**
 * Общий интерфейс для получения локализованных сообщений.
 * Сначала получаешь, потом можешь что угодно делать.
 */
public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
