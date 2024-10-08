package ru.otus.hw.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.model.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    Book toBook(BookCreateDto dto);

    BookDto toBookDto(Book book);

    Book toBook(BookDto dto);
}
