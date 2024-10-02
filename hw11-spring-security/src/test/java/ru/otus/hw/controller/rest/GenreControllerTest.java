package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.controller.utils.JsonUtils;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
class GenreControllerTest {

    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Проверка успешного получения всех жанров")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void getAllGenres_correctGet_returnGenresList () throws Exception {
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
    @DisplayName("Проверка успешного сохранения жанра")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void createGenre_correctSave_returnGenre() throws Exception {
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

    @Test
    @DisplayName("Проверка получения ошибки 403 при попытке получения списка жанров без нужных на то прав")
    @WithMockUser(
            username = "test",
            authorities = {"ROLE_TEST"}
    )
    void getAllGenres_noPermission_error403() throws Exception {
        List<GenreDto> genres = List.of(
                new GenreDto(1L, "Фантастика"),
                new GenreDto(2L, "Научпоп")
        );

        when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("http://localhost:8080/api/v1/genre"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Проверка получения ошибки 403 при попытке создания жанра без нужных на то прав")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void createGenre_noPermission_error403() throws Exception {
        GenreDto requestDto = new GenreDto(null, "Приключения");
        GenreDto responseDto = new GenreDto(1L, "Приключения");

        when(genreService.save(any(GenreDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("http://localhost:8080/api/v1/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(requestDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}