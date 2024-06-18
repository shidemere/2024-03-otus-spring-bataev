package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }


    @Override
    public List<Comment> findByBookId(String id) {
        return commentRepository.findByBookId(id);
    }

    @Override
    public Comment update(String id, String text) {
        String errMsg = "Не существует комментария для обновления";
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(errMsg));
        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Override
    public Comment create(String text, String bookId) {
        String errMsg = "Книги для создаваемого комментария не существует";
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException(errMsg));
        Comment comment = Comment.builder()
                .text(text)
                .book(book)
                .build();
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

}
