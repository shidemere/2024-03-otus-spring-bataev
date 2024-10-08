package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper {

    Author toAuthor(AuthorDto authorDto);

    AuthorDto toAuthorDto(Author author);
}
