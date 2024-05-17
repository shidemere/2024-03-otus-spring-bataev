package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "comments", schema = "public")
@ToString(exclude = {"book"})
@EqualsAndHashCode(exclude = {"book"})
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "book_entity_graph",
        attributeNodes = {
                @NamedAttributeNode(value = "book", subgraph = "subgraph.book")
        },
        subgraphs = {
                @NamedSubgraph(name = "subgraph.book",
                        attributeNodes = {
                                @NamedAttributeNode("genre"),
                                @NamedAttributeNode("author")
                        }
                )
        }
)
public class BookComment {
    @Id
    private long id;

    @Column(name = "comment_text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
