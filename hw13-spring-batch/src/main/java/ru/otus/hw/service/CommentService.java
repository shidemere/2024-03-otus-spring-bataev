package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.h2.EntityBook;
import ru.otus.hw.model.h2.EntityComment;
import ru.otus.hw.model.mongo.Comment;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MigrationCache cache;

    public EntityComment toSqlComment(Comment comment) {
        EntityComment entityComment = new EntityComment();
        entityComment.setText(comment.getText());
        Optional<EntityBook> book = Optional.ofNullable(cache.getBookCache().get(comment.getDocumentBook().getId()));
        entityComment.setBook(book.orElse(null));
        return entityComment;
    }
}
