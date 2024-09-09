package ru.otus.hw.mapper;


import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.model.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {



    Book toBook(BookCreateDto dto);

    BookCreateDto toBookCreateDto(Book book);

    Book toBook(BookUpdateDto dto);

    BookUpdateDto toBookUpdateDto(Book book);
}
