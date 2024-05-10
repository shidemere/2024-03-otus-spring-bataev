package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Получение книги по айдишнику
     */
    @Override
    public Optional<Book> findById(long id) {
        String findById = """
                SELECT b.id, b.title, b.author_id, b.genre_id, a.full_name, g.name from AUTHORS as a
                JOIN books as b ON a.ID = b.AUTHOR_ID
                JOIN genres as g ON b.GENRE_ID = g.ID
                WHERE b.id = :id
                """;
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(
                        findById,
                        namedParameters,
                        new BookRowMapper()
                ).stream()
                .findFirst();
    }

    /**
     * Получение всех книг
     */
    @Override
    public List<Book> findAll() {
        String getAll = """
                SELECT b.id, b.title, b.author_id, b.genre_id, a.full_name, g.name from AUTHORS as a
                JOIN books as b ON a.ID = b.AUTHOR_ID
                JOIN genres as g ON b.GENRE_ID = g.ID;
                        """;
        return jdbcTemplate.query(getAll, new BookRowMapper());
    }

    /**
     * Обновляет или добавляет книгу в таблицу.
     * Если объект еще не взаимодействовал с БД - его ID будет равен нулю, потому что ID мы руками не прописываем
     * И меняться он может только в самой БД.
     * Следовательно, если ID равен нулю - надо сохранить запись в БД.
     */
    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    /**
     * Удаляет книгу по айдишнику
     */
    @Override
    public void deleteById(long id) {
        String remove = "DELETE FROM books WHERE id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        jdbcTemplate.update(remove, parameterSource);
    }

    @Override
    public int count() {
        return jdbcTemplate.getJdbcTemplate().queryForObject("select count(*) from books", Integer.class);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        String insert = "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :author, :genre)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("author", book.getAuthor().getId());
        parameterSource.addValue("genre", book.getGenre().getId());

        jdbcTemplate.update(insert, parameterSource, keyHolder);

        //todo Не понимаю это замечание
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        String insert = "UPDATE books SET title = :title, author_id = :author, genre_id = :genre WHERE id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", book.getId());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("author", book.getAuthor().getId());
        parameterSource.addValue("genre", book.getGenre().getId());

        int updated = jdbcTemplate.update(insert, parameterSource);
        if (updated == 0) {
            throw new EntityNotFoundException("Не найдена сущность для обновления");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            long genreId = rs.getLong("genre_id");
            String authorName = rs.getString("full_name");
            String genreName = rs.getString("name");

            Author author = new Author();
            author.setId(authorId);
            author.setFullName(authorName);

            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(genreName);

            return new Book(id, title, author, genre);
        }
    }
}
