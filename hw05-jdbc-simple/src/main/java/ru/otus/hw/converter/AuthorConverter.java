package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.model.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
