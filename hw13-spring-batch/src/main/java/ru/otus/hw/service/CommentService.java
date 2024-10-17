package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.SQLBook;
import ru.otus.hw.model.h2.SQLComment;
import ru.otus.hw.model.mongo.Comment;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MigrationCache cache;

    public SQLComment toSqlComment(Comment comment) {
        SQLComment sqlComment = new SQLComment();
        sqlComment.setText(comment.getText());
        Optional<SQLBook> book = Optional.ofNullable(cache.getBookCache().get(comment.getBook().getId()));
        sqlComment.setBook(book.orElse(null));
        return sqlComment;
    }
}
