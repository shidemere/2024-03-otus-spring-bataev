package ru.otus.hw.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;


    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(long id) {
        List<Comment> commentList = commentRepository.findByBookId(id);
        return commentList.stream().map(commentMapper::toCommentDto).toList();
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto dto) {
        Comment saved = commentRepository.save(commentMapper.toComment(dto));
        return commentMapper.toCommentDto(saved);
    }


}
