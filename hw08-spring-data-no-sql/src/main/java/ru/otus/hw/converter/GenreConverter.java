package ru.otus.hw.converter;

import org.springframework.stereotype.Component;
import ru.otus.hw.model.Genre;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
