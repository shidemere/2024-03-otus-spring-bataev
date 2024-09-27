package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("api/v1/comment/{id}")
    public List<CommentDto> getAllCommentsByBookId(@PathVariable long id) {
        return commentService.findByBookId(id);
    }

    @PostMapping("api/v1/comment")
    public CommentDto addComment(@RequestBody CommentDto commentDto) {
        return commentService.create(commentDto);
    }

}
