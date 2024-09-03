package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.model.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookUpdateMapper {

    Book toBook(BookUpdateDto dto);

    BookUpdateDto toBookUpdateDto(Book book);
}
