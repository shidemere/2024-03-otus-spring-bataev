package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.mapper.BookCreateMapper;
import ru.otus.hw.mapper.BookUpdateMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.BookRepository;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    BookUpdateMapper bookUpdateMapper;
    @Autowired
    BookCreateMapper bookCreateMapper;



    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> books = List.of(
                new Book(1L, "Отцы и дети", new Author(1L, "Тургенев"), new Genre(1L, "Роман")),
                new Book(2L, "Илиада", new Author(2L, "Поэма"), new Genre(2L, "Поэма")),
                new Book(3L, "Книга пяти колец", new Author(3L, "Трактат"), new Genre(3L, "Трактат"))
        );
        given(bookRepository.findAll()).willReturn(books);


        mockMvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString()))
    }

    @Test
    void deleteBook() {
    }

    @Test
    void editBook() {
    }

    @Test
    void update() {
    }

    @Test
    void createBook() {
    }

    @Test
    void create() {
    }
}