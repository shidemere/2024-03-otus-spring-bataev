package ru.otus.hw.service;

import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
    GenreDto save(GenreDto genreDto);
}
