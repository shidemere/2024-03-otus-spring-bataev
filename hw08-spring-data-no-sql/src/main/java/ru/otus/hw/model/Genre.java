package ru.otus.hw.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(value = "genres")
@ToString(of = "id")
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    private String id;


    private String name;


}
