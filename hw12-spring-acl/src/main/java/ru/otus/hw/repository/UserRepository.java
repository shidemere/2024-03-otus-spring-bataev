package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.model.CustomUser;

import java.util.Optional;

/**
 * Репозиторий для работы с пользоватлеями, которые используются в Spring Security.
 */
@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
}
