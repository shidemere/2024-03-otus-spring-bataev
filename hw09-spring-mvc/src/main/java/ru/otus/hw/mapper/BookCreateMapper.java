package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.model.Book;

// Совсем уверен, стоит ли мне использовать два маппера, или использовать один на обе DTO
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookCreateMapper {

    Book toBook(BookCreateDto dto);

    BookCreateDto toBookCreateDto(Book book);

}
