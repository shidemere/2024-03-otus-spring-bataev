package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("api/v1/author")
    public List<AuthorDto> getAuthor() {
        return authorService.findAll();
    }

    @PostMapping("api/v1/author")
    public AuthorDto addAuthor(@Valid @RequestBody AuthorDto dto) {
        return authorService.save(dto);
    }

}
