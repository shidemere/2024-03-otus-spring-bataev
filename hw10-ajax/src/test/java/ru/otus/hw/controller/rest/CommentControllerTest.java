package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.controller.utils.JsonUtils;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.service.GenreService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc
class CommentControllerTest {


    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCommentsByBookId() throws Exception {
        BookDto dto = new BookDto(
                1L, "Какая то книга", new AuthorDto(1L, "Имя автора"), new GenreDto(1L, "Имя жанра")
        );
        List<CommentDto> comments = List.of(
                new CommentDto(1L, "Класс", new BookDto()),
                new CommentDto(2L, "Отстой", new BookDto())
        );

        when(commentService.findByBookId(dto.getId())).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/comment/{id}", dto.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Класс"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].text").value("Отстой"));
    }

    @Test
    void addComment() throws Exception {
        BookDto dto = new BookDto(
                1L, "Какая то книга", new AuthorDto(1L, "Имя автора"), new GenreDto(1L, "Имя жанра")
        );
        CommentDto requestDto = new CommentDto(null, "Круто!", dto);
        CommentDto responseDto = new CommentDto(1L, "Круто!", dto);

        when(commentService.create(any(CommentDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("http://localhost:8080/api/v1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(requestDto))) // Используем JsonUtils для преобразования
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Круто!"))
                .andExpect(jsonPath("$.book.title").value("Какая то книга"));
    }
}