package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.BookComment;

@Component
@RequiredArgsConstructor
public class BookCommentConverter {

    private final BookConverter bookConverter;

    public String bootCommentToString(BookComment comment) {
        return "Id: %d, FullText: %s, Book%s:".formatted(
                comment.getId(),
                comment.getText(),
                bookConverter.bookToString(comment.getBook())
        );
    }

}
