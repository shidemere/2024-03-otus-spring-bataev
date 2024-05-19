package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    public String bootCommentToString(Comment comment) {
        return "Id: %d, FullText: %s".formatted(
                comment.getId(),
                comment.getText()
        );
    }

}
