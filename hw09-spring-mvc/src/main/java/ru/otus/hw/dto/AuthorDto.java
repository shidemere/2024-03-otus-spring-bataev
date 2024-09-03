package ru.otus.hw.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class AuthorDto {

    private Long id;

    private String fullName;
}