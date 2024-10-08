package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.CommentRepository;
import ru.otus.hw.service.security.GrantGivingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final GrantGivingService grantService;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'READ')")
    public List<CommentDto> findByBookId(long id) {
        List<Comment> commentList = commentRepository.findByBookId(id);
        return new ArrayList<>(commentList.stream().map(commentMapper::toCommentDto).toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public CommentDto create(CommentCreateDto dto) {
        Comment saved = commentRepository.save(commentMapper.toComment(dto));

        //выдача прав
        grantService.setReadGrant(saved);
        grantService.setWriteGrant(saved);
        grantService.setCreateGrant(saved);
        grantService.setDeleteGrant(saved);
        grantService.setAdminGrant(saved);

        return commentMapper.toCommentDto(saved);
    }


}
