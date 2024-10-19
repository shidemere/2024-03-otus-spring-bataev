package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);
    @Mapping(source = "bookId", target = "book.id")
    Comment toComment(CommentCreateDto dto);

}
