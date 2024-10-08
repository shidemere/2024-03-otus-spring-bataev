package ru.otus.hw.controller.rest;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.controller.rest.BookController;
import ru.otus.hw.controller.utils.JsonUtils;
import ru.otus.hw.dto.*;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.service.BookService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
class BookControllerTest {


    @MockBean
    private BookService bookService;
    @MockBean
    private BookMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Проверка корректного получения списка книг")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void getBooks_correctGet_returnBooks() throws Exception {
        List<BookDto> books = List.of(
                new BookDto(1L, "Book One", new AuthorDto(1L, "Author One"), new GenreDto(1L, "Genre One")),
                new BookDto(2L, "Book Two", new AuthorDto(2L, "Author Two"), new GenreDto(2L, "Genre Two"))
        );

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/book"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Book One"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Book Two"));
    }


    @Test
    @DisplayName("Проверка получения ошибки 404 при поптыке получения книги в БД с использованием нужных прав")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void shouldReturn404WhenBookNotFoundWhileGettingBooks() throws Exception {
        long bookId = 16L;
        when(bookService.findById(bookId)).thenThrow(new EntityNotFoundException("Book not found"));

        // Выполнение GET-запроса и проверка результата
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book/{id}", bookId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Book not found"));
    }

    @Test
    @DisplayName("Проверка получения ошибки 404 при сохранении книги в БД с использованием нужных прав")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void shouldReturn400WhenRuntimeExceptionInCreate() throws Exception {
        BookCreateDto createDto = new BookCreateDto("Какое то название", 4L, 4L);
        when(bookService.create(Mockito.any(BookCreateDto.class))).thenThrow(new RuntimeException("Runtime exception occurred"));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtils.toJson(createDto))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Runtime exception occurred"));
    }

    @Test
    @DisplayName("Проверка успешного получения книги при запросе")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void getBookById_correctGet_returnBook() throws Exception {
        long bookId = 1L;
        BookDto book = new BookDto(bookId, "Книга", new AuthorDto(1L, "Автор"), new GenreDto(1L, "Жанр"));

        when(bookService.findById(bookId)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/book/{id}", bookId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Книга"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.fullName").value("Автор"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre.name").value("Жанр"));
    }


    @Test
    @DisplayName("Проверка успешного сохранения книги")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void createBook_correctSave_returnBook() throws Exception {
        BookCreateDto requestDto = new BookCreateDto("Книга", 1L, 2L);
        BookDto responseDto = new BookDto(1L, "Книга", new AuthorDto(1L, "Автор один"), new GenreDto(2L, "Жанр два"));

        when(bookService.create(Mockito.any(BookCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(requestDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Книга"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.fullName").value("Автор один"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre.name").value("Жанр два"));
    }


    @Test
    @DisplayName("Проверка успешного обновления при наличии нужных прав")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void updateBook_correctUpdate_returnBook() throws Exception {
        BookUpdateDto requestDto = new BookUpdateDto(1L, "Обновленная книга", 1L, 2L);
        BookDto responseDto = new BookDto(1L, "Обновленная книга", new AuthorDto(1L, "Автор один"), new GenreDto(2L, "Жанр два"));

        when(bookService.update(Mockito.any(BookUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/api/v1/book/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(requestDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Обновленная книга"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.fullName").value("Автор один"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre.name").value("Жанр два"));
    }


    @Test
    @DisplayName("Проверка успешного удаления книги")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void deleteBook_correctDelete_returnNothing() throws Exception {
        Mockito.doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/v1/book/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}