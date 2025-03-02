package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.BookService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/author/{authorName}")
    public List<Book> getBooksByAuthorName(@PathVariable String authorName) {
        return bookService.getBooksByAuthorName(authorName);
    }

    @GetMapping("/primary-author/{authorId}")
    public List<Book> getBooksByPrimaryAuthor(@PathVariable Long authorId) {
        return bookService.getBooksByPrimaryAuthor(authorId);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}

