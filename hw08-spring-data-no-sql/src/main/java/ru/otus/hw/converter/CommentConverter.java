package ru.otus.hw.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentConverter {



    public String bootCommentToString(Comment comment) {
        return "Id: %s, FullText: %s, Book ID: %s".formatted(
                comment.getId(),
                comment.getText(),
                comment.getBook().getId()
        );
    }

}
