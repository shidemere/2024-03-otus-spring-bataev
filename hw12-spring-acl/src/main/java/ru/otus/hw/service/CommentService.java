package ru.otus.hw.service;

import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findByBookId(long id);
    CommentDto create(CommentCreateDto dto);

}
