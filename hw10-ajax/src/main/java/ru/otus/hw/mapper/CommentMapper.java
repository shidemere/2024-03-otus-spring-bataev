package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {BookMapper.class})
public interface CommentMapper {

    Comment toComment(CommentDto dto);
    CommentDto toCommentDto(Comment comment);
}
