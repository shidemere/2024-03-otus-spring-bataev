package ru.otus.hw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private long id;

    private String name;

}
