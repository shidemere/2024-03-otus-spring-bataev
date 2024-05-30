package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exception.EntityNotFoundException;
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


    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByBookId(long id) {
        return commentRepository.findByBookId(id);
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        String errMsg = "Не существует комментария для обновления";
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(errMsg));
        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment create(String text, long bookId) {
        String errMsg = "Нет книги, для добавления комментария";
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException(errMsg));
        Comment comment = Comment.builder()
                .text(text)
                .book(book)
                .build();
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
