package com.bookmanagmentapp.bookmanagmentapplication.controller.bookcontrollers;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.BookSearchService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookSearchController {
    private final BookSearchService bookSearchService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookSearchService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookSearchService.getBookById(id);
    }

    @GetMapping("/show-book/author/{authorName}")
    public List<Book> getBooksByAuthorName(@PathVariable String authorName) {
        return bookSearchService.getBooksByAuthorName(authorName);
    }
}

