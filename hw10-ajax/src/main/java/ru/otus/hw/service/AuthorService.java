package ru.otus.hw.service;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
    AuthorDto save(AuthorDto authorDto);
}
