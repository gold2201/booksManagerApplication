package com.bookmanagmentapp.bookmanagmentapplication.controller.authorconrtollers;

import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.service.authorservices.AuthorSearchService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
public class AuthorSearchController {
    private final AuthorSearchService authorSearchService;

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorSearchService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorSearchService.getAuthorById(id);
    }

    @GetMapping("/by-book")
    public List<Author> getAuthorsByBookTitle(@RequestParam String bookTitle) {
        return authorSearchService.getAuthorsByBookTitle(bookTitle);
    }
}

