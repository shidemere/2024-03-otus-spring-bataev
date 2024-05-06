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
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final JdbcGenreRepository jdbcGenreRepository;

    private final JdbcAuthorRepository jdbcAuthorRepository;

    /**
     * Получение книги по айдишнику
     */
    @Override
    public Optional<Book> findById(long id) {
        String findById = "SELECT id, title, author_id, genre_id FROM books WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        Book book = jdbcTemplate.queryForObject(
                findById,
                namedParameters,
                new BookRowMapper()
        );

        if (Objects.isNull(book)) {
            return Optional.empty();
        }


        Optional<Author> author = jdbcAuthorRepository.findById(book.getAuthor().getId());
        Optional<Genre> genre = jdbcGenreRepository.findById(book.getGenre().getId());
        book.setAuthor(author.orElse(new Author()));
        book.setGenre(genre.orElse(new Genre()));

        return Optional.of(book);
    }

    /**
     * Получение всех книг
     */
    @Override
    public List<Book> findAll() {
        String getAll = "SELECT id, title, author_id, genre_id FROM books";
        List<Book> books = jdbcTemplate.query(getAll, new BookRowMapper());

        return books.stream()
                .map(book -> {
                    Optional<Author> author = jdbcAuthorRepository.findById(book.getAuthor().getId());
                    Optional<Genre> genre = jdbcGenreRepository.findById(book.getGenre().getId());
                    book.setAuthor(author.orElse(new Author()));
                    book.setGenre(genre.orElse(new Genre()));
                    return book;
                }).collect(Collectors.toList());
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

            Author author = new Author();
            author.setId(authorId);

            Genre genre = new Genre();
            genre.setId(genreId);

            return new Book(id, title, author, genre);
        }
    }
}
