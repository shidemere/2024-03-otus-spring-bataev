package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.SQLAuthor;
import ru.otus.hw.model.mongo.Author;


@Service
@RequiredArgsConstructor
public class AuthorService {

    private final MigrationCache cache;

    public SQLAuthor toSqlAuthor(Author author) {
        SQLAuthor sqlAuthor = new SQLAuthor();
        sqlAuthor.setFullName(author.getFullName());
        cache.getAuthorCache().put(author.getId(), sqlAuthor);
        return sqlAuthor;
    }

}
