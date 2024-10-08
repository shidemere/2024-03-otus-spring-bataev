package ru.otus.hw.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.CustomUser;
import ru.otus.hw.repository.UserRepository;
/**
 * Кастомная реализация UserDetailsService для Spring Security.
 * Автоматически подтянется в Spring и будет использовано.
 * Как Spring выбирает между несколькими? Мне пока не совсем понятно.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser customUser = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );

        return User.builder()
                .username(customUser.getUsername())
                .password(customUser.getPassword())
                .roles(customUser.getRole())
                .build();
    }
}
