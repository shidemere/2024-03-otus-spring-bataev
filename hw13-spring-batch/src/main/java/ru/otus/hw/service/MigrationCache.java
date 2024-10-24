package ru.otus.hw.service;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.h2.EntityAuthor;
import ru.otus.hw.model.h2.EntityBook;
import ru.otus.hw.model.h2.EntityComment;
import ru.otus.hw.model.h2.EntityGenre;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Благодаря GC мне не придётся думать о том, чтобы как то чистить кэш.
 */
@Getter
@Component
public class MigrationCache {

    // Кэш для каждой сущности
    private final ConcurrentHashMap<String, EntityGenre> genreCache = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, EntityAuthor> authorCache = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, EntityBook> bookCache = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, EntityComment> commentCache = new ConcurrentHashMap<>();

}
