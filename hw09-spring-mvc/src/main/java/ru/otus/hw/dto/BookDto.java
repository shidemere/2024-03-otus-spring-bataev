package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class BookDto {

    private Long id;

    @NotBlank(message = "The book title must contain at least one simbol.")
    @Length(min = 3, message = "The book title cannot be shorter than three characters.")
    private String title;

    @NotNull
    private AuthorDto author;

    @NotNull
    private GenreDto genre;
}
