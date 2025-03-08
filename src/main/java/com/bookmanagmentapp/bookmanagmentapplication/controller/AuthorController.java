package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.service.AuthorService;
import com.bookmanagmentapp.bookmanagmentapplication.service.BookService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("/by-book")
    public List<Author> getAuthorsByBookTitle(@RequestParam String bookTitle) {
        return authorService.getAuthorsByBookTitle(bookTitle);
    }

    @GetMapping("/primary-by-book")
    public Author getPrimaryAuthorByBookTitle(@RequestParam String bookTitle) {
        return bookService.getPrimaryAuthorByBookTitle(bookTitle);
    }

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorService.saveAuthor(author);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
}

