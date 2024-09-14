package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.dto.*;
import ru.otus.hw.mapper.AuthorMapperImpl;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.BookMapperImpl;
import ru.otus.hw.mapper.GenreMapperImpl;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@Import({
        BookMapperImpl.class,
        AuthorMapperImpl.class,
        GenreMapperImpl.class
})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    List<Book> books;

    @MockBean
    private BookService bookService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;
    @Autowired
    BookMapper bookMapper;

    @BeforeEach
    public void setup() {
        books = List.of(
                new Book(1L, "Отцы и дети", new Author(1L, "Тургенев"), new Genre(1L, "Роман")),
                new Book(2L, "Илиада", new Author(2L, "Поэма"), new Genre(2L, "Поэма")),
                new Book(3L, "Книга пяти колец", new Author(3L, "Трактат"), new Genre(3L, "Трактат"))
        );


    }

    @Test
    @DisplayName("Книги корректно получаются")
    void shouldReturnCorrectBooksList() throws Exception {
        List<BookDto> dtoList = books.stream().map(bookMapper::toBookDto).toList();
        Mockito.when(bookService.findAll()).thenReturn(dtoList);

        mockMvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("list"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", dtoList));
    }


    @Test
    @DisplayName("Книги корректно удаляются")
    void shouldCorrectDeleteBook() throws Exception {
        List<BookDto> dtoList = books.stream().map(bookMapper::toBookDto).toList();
        long bookId = 1L;

        Mockito.doNothing().when(bookService).deleteById(bookId);
        Mockito.when(bookService.findAll()).thenReturn(dtoList);

        mockMvc.perform(get("/delete/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("list"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", dtoList));

        Mockito.verify(bookService, Mockito.times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Книга корректно загружается для редактирования")
    void shouldLoadBookForEdit() throws Exception {

        long bookId = 1L;
        BookDto book = bookMapper.toBookDto(books.get(0));
        List<Author> authors = List.of(new Author(1L, "Тургенев"));
        List<Genre> genres = List.of(new Genre(1L, "Роман"));

        Mockito.when(bookService.findById(bookId)).thenReturn(book);
        Mockito.when(authorService.findAll()).thenReturn(authors);
        Mockito.when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("/edit/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("edit"))
                .andExpect(MockMvcResultMatchers.model().attribute("book", book))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authors))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genres));

        Mockito.verify(bookService, Mockito.times(1)).findById(bookId);
        Mockito.verify(authorService, Mockito.times(1)).findAll();
        Mockito.verify(genreService, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Книга корректно обновляется")
    void shouldUpdateBook() throws Exception {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(1L, "Отцы и дети", 1L, 1L);
        BookDto updatedBook = new BookDto(1L, "Отцы и дети", new AuthorDto(1L, "Тургенев"), new GenreDto(1L, "Роман"));

        Mockito.when(bookService.update(bookUpdateDto)).thenReturn(updatedBook);

        mockMvc.perform(post("/edit/book/{id}", bookUpdateDto.getId())
                        .flashAttr("bookUpdateDto", bookUpdateDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/list"));

        Mockito.verify(bookService, Mockito.times(1)).update(bookUpdateDto);
    }

    @Test
    @DisplayName("Форма для создания книги корректно загружается")
    void shouldLoadCreateBookForm() throws Exception {
        List<Author> authors = List.of(new Author(1L, "Тургенев"));
        List<Genre> genres = List.of(new Genre(1L, "Роман"));

        Mockito.when(authorService.findAll()).thenReturn(authors);
        Mockito.when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("/create/book"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create"))
                .andExpect(MockMvcResultMatchers.model().attribute("authors", authors))
                .andExpect(MockMvcResultMatchers.model().attribute("genres", genres));

        Mockito.verify(authorService, Mockito.times(1)).findAll();
        Mockito.verify(genreService, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Книга корректно создается")
    void shouldCreateBook() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto("Отцы и дети", 1L, 1L);
        BookDto createdBook = new BookDto(1L, "Отцы и дети", new AuthorDto(1L, "Тургенев"), new GenreDto(1L, "Роман"));

        Mockito.when(bookService.create(bookCreateDto)).thenReturn(createdBook);

        mockMvc.perform(post("/create/book")
                        .flashAttr("bookCreateDto", bookCreateDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/list"));

        Mockito.verify(bookService, Mockito.times(1)).create(bookCreateDto);
    }
}