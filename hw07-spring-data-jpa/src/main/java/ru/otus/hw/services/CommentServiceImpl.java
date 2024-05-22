package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

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
        Optional<Comment> comment = commentRepository.findById(id);
        String errMsg = "Не существует комментария для обновления";
        comment.orElseThrow(() -> new EntityNotFoundException(errMsg)).setText(text);
        return commentRepository.save(comment.get());
    }

    @Override
    @Transactional
    public Comment insert(String text, long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        Comment comment = Comment.builder()
                .text(text)
                .book(book.orElseThrow(() -> new EntityNotFoundException("Нет книги, для добавления комментария")))
                .build();
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
