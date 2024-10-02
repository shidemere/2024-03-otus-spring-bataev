package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    /**
     * Трназкционно считывает данные из БД.
     * @return лист жанров.
     */
    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        List<Genre> responseList = genreRepository.findAll();
        return responseList.stream().map(genreMapper::toGenreDto).toList();
    }

    @Override
    public GenreDto save(GenreDto genreDto) {
        Genre saved = genreRepository.save(genreMapper.toGenre(genreDto));
        return genreMapper.toGenreDto(saved);
    }
}
