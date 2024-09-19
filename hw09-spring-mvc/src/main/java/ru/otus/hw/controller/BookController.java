package ru.otus.hw.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/list")
    public String getBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAllAttributes(Map.of("books", books));
        return "list";
    }

    @GetMapping("delete/book/{id}")
    public String deleteBook(@PathVariable long id, Model model) {
        bookService.deleteById(id);
        List<BookDto> books = bookService.findAll();
        model.addAllAttributes(Map.of("books", books));
        return "list";
    }

    @GetMapping("edit/book/{id}")
    public String editBook(@PathVariable long id, Model model) {
        BookDto book = bookService.findById(id);
        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "edit";
    }

    @PostMapping("/edit/book/{id}")
    public String update(@Valid BookUpdateDto bookUpdateDto, BindingResult bindingResult, @PathVariable long id ) {
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/edit/book/%d", id);
        }
        bookUpdateDto.setId(id);
        bookService.update(bookUpdateDto);

        return "redirect:/list";
    }

    @GetMapping("/create/book")
    public String createBook(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "create";
    }

    @PostMapping("/create/book")
    public String create(@Valid BookCreateDto bookCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/create/book";
        }
        bookService.create(bookCreateDto);

        return "redirect:/list";
    }
}
