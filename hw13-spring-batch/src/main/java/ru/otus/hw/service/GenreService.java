package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.EntityGenre;
import ru.otus.hw.model.mongo.DocumentGenre;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final MigrationCache cache;

    public EntityGenre toSqlGenre(DocumentGenre documentGenre) {
        EntityGenre sqlgenre = new EntityGenre();
        sqlgenre.setName(documentGenre.getName());
        cache.getGenreCache().put(documentGenre.getId(), sqlgenre);
        return sqlgenre;
    }
}
