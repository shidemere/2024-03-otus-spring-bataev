package ru.otus.hw.service;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.h2.SQLAuthor;
import ru.otus.hw.model.h2.SQLBook;
import ru.otus.hw.model.h2.SQLComment;
import ru.otus.hw.model.h2.SQLGenre;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Благодаря GC мне не придётся думать о том, чтобы как то чистить кэш.
 */
@Getter
@Component
public class MigrationCache {

    // Кэш для каждой сущности
    private final ConcurrentHashMap<String, SQLGenre> genreCache = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, SQLAuthor> authorCache = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, SQLBook> bookCache = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, SQLComment> commentCache = new ConcurrentHashMap<>();

}
