package ru.otus.hw.controller.rest;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.controller.utils.JsonUtils;
import ru.otus.hw.dto.*;
import ru.otus.hw.service.BookService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {


    @MockBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getBooks() throws Exception {
        List<BookDto> books = List.of(
                new BookDto(1L, "Book One", new AuthorDto(1L, "Author One"), new GenreDto(1L, "Genre One")),
                new BookDto(2L, "Book Two", new AuthorDto(2L, "Author Two"), new GenreDto(2L, "Genre Two"))
        );

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("http://localhost:8080/api/v1/book"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Book Two"));
    }

    @Test
    void getBookById() throws Exception {
        long bookId = 1L;
        BookDto book = new BookDto(bookId, "Книга", new AuthorDto(1L, "Автор"), new GenreDto(1L, "Жанр"));

        when(bookService.findById(bookId)).thenReturn(book);

        mockMvc.perform(get("http://localhost:8080/api/v1/book/{id}", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Книга"))
                .andExpect(jsonPath("$.author.fullName").value("Автор"))
                .andExpect(jsonPath("$.genre.name").value("Жанр"));
    }

    @Test
    void shouldReturn404WhenBookNotFoundWhileGettingBooks() throws Exception {
        long bookId = 16L;
        when(bookService.findById(bookId)).thenThrow(new EntityNotFoundException("Book not found"));

        // Выполнение GET-запроса и проверка результата
        mockMvc.perform(get("/api/v1/book/{id}", bookId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found"));
    }

    @Test
    void shouldReturn400WhenRuntimeExceptionInCreate() throws Exception {
        BookCreateDto createDto = new BookCreateDto("Какое то название", 4L, 4L);
        when(bookService.create(Mockito.any(BookCreateDto.class))).thenThrow(new RuntimeException("Runtime exception occurred"));

        mockMvc.perform(
                    post("/api/v1/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(createDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Runtime exception occurred"));
    }

    @Test
    void createBook() throws Exception {
        BookCreateDto requestDto = new BookCreateDto("Книга", 1L, 2L);
        BookDto responseDto = new BookDto(1L, "Книга", new AuthorDto(1L, "Автор один"), new GenreDto(2L, "Жанр два"));

        when(bookService.create(Mockito.any(BookCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("http://localhost:8080/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Книга"))
                .andExpect(jsonPath("$.author.fullName").value("Автор один"))
                .andExpect(jsonPath("$.genre.name").value("Жанр два"));
    }

    @Test
    void updateBook() throws Exception {
        BookUpdateDto requestDto = new BookUpdateDto(1L, "Обновленная книга", 1L, 2L);
        BookDto responseDto = new BookDto(1L, "Обновленная книга", new AuthorDto(1L, "Автор один"), new GenreDto(2L, "Жанр два"));

        when(bookService.update(Mockito.any(BookUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("http://localhost:8080/api/v1/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Обновленная книга"))
                .andExpect(jsonPath("$.author.fullName").value("Автор один"))
                .andExpect(jsonPath("$.genre.name").value("Жанр два"));
    }

    @Test
    void deleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(delete("http://localhost:8080/api/v1/book/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }
}