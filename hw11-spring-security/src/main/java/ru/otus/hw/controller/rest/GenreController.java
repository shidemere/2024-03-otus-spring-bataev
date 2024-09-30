package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genre")
    public List<GenreDto> getAllGenres() {
        return genreService.findAll();
    }

    @PostMapping("api/v1/genre")
    public GenreDto createGenre(@RequestBody GenreDto genreDto) {
        return genreService.save(genreDto);
    }
}
