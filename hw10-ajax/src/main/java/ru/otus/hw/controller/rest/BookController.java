package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("api/v1/book")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("api/v1/book/{id}")
    public BookDto getBookById(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PutMapping("api/v1/book/{id}")
    public BookDto update(@RequestBody BookUpdateDto bookUpdateDto) {
        return bookService.update(bookUpdateDto);
    }

    @DeleteMapping("api/v1/book/{id}")
    public void delete(@PathVariable long id) {
        bookService.deleteById(id);
    }

    @PostMapping("api/v1/book")
    public BookDto create(@RequestBody BookCreateDto bookDto) {
        return bookService.create(bookDto);
    }


}
