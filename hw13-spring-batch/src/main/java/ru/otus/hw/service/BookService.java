package ru.otus.hw.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.EntityAuthor;
import ru.otus.hw.model.h2.EntityBook;
import ru.otus.hw.model.h2.EntityGenre;
import ru.otus.hw.model.mongo.DocumentBook;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final MigrationCache migrationCache;

    public EntityBook toSqlBook(DocumentBook documentBook) {
        EntityBook entityBook = new EntityBook();
        entityBook.setTitle(documentBook.getTitle());
        EntityAuthor entityAuthor = migrationCache.getAuthorCache().get(documentBook.getDocumentAuthor().getId());
        EntityGenre entityGenre = migrationCache.getGenreCache().get(documentBook.getDocumentGenre().getId());

        if (Objects.isNull(entityAuthor) || Objects.isNull(entityGenre)) {
            String msg = String.format("Для {book=%s} не найден зависимый параметр. {author=%s} | {genre=%s}",
                    documentBook.getTitle(), entityAuthor, entityGenre);
            throw new EntityNotFoundException(msg);
        }

        entityBook.setAuthor(entityAuthor);
        entityBook.setGenre(entityGenre);
        migrationCache.getBookCache().put(documentBook.getId(), entityBook);
        return entityBook;
    }

}
