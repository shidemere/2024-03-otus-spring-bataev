package ru.otus.hw.model.h2;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "authors", schema = "public")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class EntityAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authors_seq")
    @SequenceGenerator(name = "authors_seq", sequenceName = "seq_authors", allocationSize = 10)
    private long id;

    @Column(name = "full_name")
    private String fullName;
}
