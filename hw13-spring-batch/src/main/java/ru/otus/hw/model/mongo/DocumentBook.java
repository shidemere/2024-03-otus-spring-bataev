package ru.otus.hw.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Builder
@ToString(of = "id")
@Document(value = "books")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class DocumentBook {
    @Id
    private String id;

    private String title;

    @DBRef(lazy = true)
    private DocumentAuthor documentAuthor;

    @DBRef(lazy = true)
    private DocumentGenre documentGenre;

}

