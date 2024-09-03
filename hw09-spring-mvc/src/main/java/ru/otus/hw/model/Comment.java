package ru.otus.hw.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Builder
@ToString(of = "id")
@Table(name = "comments", schema = "public")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(name = "book_entity_graph", attributeNodes = {
        @NamedAttributeNode("book"),
})
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "comment_text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

}
