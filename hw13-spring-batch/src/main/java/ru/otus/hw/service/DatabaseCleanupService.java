package ru.otus.hw.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseCleanupService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleanupService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE AUTHORS");
        jdbcTemplate.execute("TRUNCATE TABLE GENRES");
        jdbcTemplate.execute("TRUNCATE TABLE BOOKS");
        jdbcTemplate.execute("TRUNCATE TABLE COMMENTS");
    }
}
