package ru.otus.hw.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GenreDto {

    private Long id;

    private String name;
}