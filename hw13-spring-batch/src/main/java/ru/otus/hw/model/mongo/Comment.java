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
@Builder
@ToString(of = "id")
@Document(value = "comments")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Comment {

    @Id
    private String id;

    private String text;

    private DocumentBook documentBook;
}
