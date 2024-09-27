package ru.otus.hw.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;

    private String text;

    @NotNull
    private BookDto book;

}
