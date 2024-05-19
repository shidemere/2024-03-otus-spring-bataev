package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    /**
     * Делается в 1 запрос.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    /**
     * Делается в 2 запроса. Как уместить в 1?
     */
    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByBookId(long id) {
        return commentRepository.findByBookId(id);
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        Comment comment = Comment.builder()
                .id(id)
                .text(text)
                .build();
        Optional<Comment> optional = commentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Не найден комментарий для обновлений");
        }
        return commentRepository.update(comment);
    }

    @Override
    @Transactional
    public Comment insert(String text, long bookId) {
        Comment comment = Comment.builder()
                .text(text)
                .build();

        return commentRepository.insert(comment, bookId);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
