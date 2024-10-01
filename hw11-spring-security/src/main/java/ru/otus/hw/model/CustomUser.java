package ru.otus.hw.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Пользователь для аутентификации и авторизации в Spring Security.
 */

@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}
