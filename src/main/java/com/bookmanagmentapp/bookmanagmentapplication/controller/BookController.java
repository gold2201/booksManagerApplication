package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.dto.BookAuthorsUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.BookService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/show-book/author/{authorName}")
    public List<Book> getBooksByAuthorName(@PathVariable String authorName) {
        return bookService.getBooksByAuthorName(authorName);
    }

    @PostMapping("/create-book")
    public Book createBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("/rename-author/{id}")
    public Book updateAuthors(@PathVariable Long id, @RequestBody BookAuthorsUpdateDto dto) {
        return bookService.updateAuthor(id, dto.getOldAuthorName(), dto.getNewAuthorName());
    }

    @PutMapping("/rename-title/{id}")
    public Book updateBookTitle(@PathVariable Long id, @RequestBody BookTitleUpdateDto dto) {
        return bookService.updateBookTitle(id, dto);
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}

