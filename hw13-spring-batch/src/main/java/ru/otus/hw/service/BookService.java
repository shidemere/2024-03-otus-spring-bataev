package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.SQLAuthor;
import ru.otus.hw.model.h2.SQLBook;
import ru.otus.hw.model.h2.SQLGenre;
import ru.otus.hw.model.mongo.Book;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final MigrationCache migrationCache;

    public SQLBook toSqlBook(Book book) {
        SQLBook sqlBook = new SQLBook();
        sqlBook.setTitle(book.getTitle());
        Optional<SQLAuthor> sqlAuthor = Optional.ofNullable(migrationCache.getAuthorCache().get(book.getAuthor().getId()));
        Optional<SQLGenre> sqlGenre = Optional.ofNullable(migrationCache.getGenreCache().get(book.getGenre().getId()));
        sqlBook.setAuthor(sqlAuthor.orElse(null));
        sqlBook.setGenre(sqlGenre.orElse(null));
        migrationCache.getBookCache().put(book.getId(), sqlBook);
        return sqlBook;
    }

}
