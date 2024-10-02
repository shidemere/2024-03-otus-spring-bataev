package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {

    Genre toGenre(GenreDto genreDto);

    GenreDto toGenreDto(Genre genre);
}
