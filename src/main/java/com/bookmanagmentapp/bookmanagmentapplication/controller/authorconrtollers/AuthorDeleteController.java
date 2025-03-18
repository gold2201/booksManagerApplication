package com.bookmanagmentapp.bookmanagmentapplication.controller.authorconrtollers;

import com.bookmanagmentapp.bookmanagmentapplication.service.authorservices.AuthorDeleteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
public class AuthorDeleteController {
    private final AuthorDeleteService authorDeleteService;

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorDeleteService.deleteAuthor(id);
    }
}
