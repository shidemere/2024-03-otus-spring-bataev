package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.EntityAuthor;
import ru.otus.hw.model.mongo.DocumentAuthor;


@Service
@RequiredArgsConstructor
public class AuthorService {

    private final MigrationCache cache;

    public EntityAuthor toSqlAuthor(DocumentAuthor documentAuthor) {
        EntityAuthor entityAuthor = new EntityAuthor();
        entityAuthor.setFullName(documentAuthor.getFullName());
        cache.getAuthorCache().put(documentAuthor.getId(), entityAuthor);
        return entityAuthor;
    }

}
