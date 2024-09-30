package ru.otus.hw.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.model.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

//    @Mapping(source = "authorId", target = "author.id")
//    @Mapping(source = "genreId", target = "genre.id")
    Book toBook(BookCreateDto dto);

//    @Mapping(source = "author.id", target = "authorId")
//    @Mapping(source = "genre.id", target = "genreId")
    BookCreateDto toBookCreateDto(Book book);

//    @Mapping(source = "authorId", target = "author.id")
//    @Mapping(source = "genreId", target = "genre.id")
    Book toBook(BookUpdateDto dto);

//    @Mapping(source = "author.id", target = "authorId")
//    @Mapping(source = "genre.id", target = "genreId")
    BookUpdateDto toBookUpdateDto(Book book);

//    @Mapping(source = "author.id", target = "authorId")
//    @Mapping(source = "genre.id", target = "genreId")
    BookDto toBookDto(Book book);
}
