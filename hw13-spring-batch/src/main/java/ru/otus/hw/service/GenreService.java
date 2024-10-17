package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.SQLGenre;
import ru.otus.hw.model.mongo.Genre;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final MigrationCache cache;

    public SQLGenre toSqlGenre(Genre genre) {
        SQLGenre sqlgenre = new SQLGenre();
        sqlgenre.setName(genre.getName());
        cache.getGenreCache().put(genre.getId(), sqlgenre);
        return sqlgenre;
    }
}
