package ru.otus.hw.service;

import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<CommentDto> findByBookId(long id);
    CommentDto create(CommentCreateDto dto);

}
