package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "books", schema = "public")
@ToString(exclude = {"author", "genre", "comment"})
@EqualsAndHashCode(exclude = {"author", "genre", "comment"})
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "genre_author_comment_entity_graph", attributeNodes = {
        @NamedAttributeNode("author"),
        @NamedAttributeNode("genre"),
})
public class Book {
    @Id
    private long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "book")
    private List<BookComment> comment;
}

