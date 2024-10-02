package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.utils.JsonUtils;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class)
@AutoConfigureMockMvc
class GenreControllerTest {

    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllGenres() throws Exception {
        List<GenreDto> genres = List.of(
                new GenreDto(1L, "Фантастика"),
                new GenreDto(2L, "Научпоп")
        );

        when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("http://localhost:8080/api/v1/genre"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Фантастика"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Научпоп"));
    }

    @Test
    void createGenre() throws Exception {
        GenreDto requestDto = new GenreDto(null, "Приключения");
        GenreDto responseDto = new GenreDto(1L, "Приключения");

        when(genreService.save(any(GenreDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("http://localhost:8080/api/v1/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Приключения"));
    }
}