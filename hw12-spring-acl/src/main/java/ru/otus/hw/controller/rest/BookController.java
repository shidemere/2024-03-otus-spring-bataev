package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("api/v1/book")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("api/v1/book/{id}")
    public BookDto getBookById(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PutMapping("api/v1/book/{id}")
    public BookDto update(@Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return bookService.update(bookUpdateDto);
    }

    @DeleteMapping("api/v1/book/{id}")
    public void delete(@PathVariable long id) {
        bookService.deleteById(id);
    }

    @PostMapping("api/v1/book")
    public BookDto create(@Valid @RequestBody BookCreateDto bookDto) {
        return bookService.create(bookDto);
    }


}
