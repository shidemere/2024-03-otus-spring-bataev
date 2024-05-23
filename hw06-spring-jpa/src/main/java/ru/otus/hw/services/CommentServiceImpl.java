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
        Optional<Book> book = bookRepository.findById(id);
        return commentRepository.findByBookId(book.orElseThrow());
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        Comment comment = Comment.builder()
                .id(id)
                .text(text)
                .build();
        return commentRepository.update(comment);
    }

    @Override
    @Transactional
    public Comment create(String text, long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        String errMsg = "Книга, для которой устанавливается комментарий, не найдена";
        Comment comment = Comment.builder()
                .text(text)
                .book(book.orElseThrow(() -> new EntityNotFoundException(errMsg)))
                .build();

        return commentRepository.create(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
