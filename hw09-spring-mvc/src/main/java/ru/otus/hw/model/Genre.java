package ru.otus.hw.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
@Table(name = "genres", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

}