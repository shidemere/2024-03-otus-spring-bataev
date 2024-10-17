package ru.otus.hw.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(value = "authors")
@EqualsAndHashCode(of = "id")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    private String id;

    private String fullName;

}
