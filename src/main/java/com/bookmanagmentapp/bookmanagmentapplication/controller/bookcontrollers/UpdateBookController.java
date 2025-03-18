package com.bookmanagmentapp.bookmanagmentapplication.controller.bookcontrollers;

import com.bookmanagmentapp.bookmanagmentapplication.dto.BookAuthorsUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.UpdateBookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class UpdateBookController {
    private final UpdateBookService updateBookService;

    @PutMapping("/rename-author/{id}")
    public Book updateAuthors(@PathVariable Long id, @RequestBody BookAuthorsUpdateDto dto) {
        return updateBookService.updateAuthor(id, dto.getOldAuthorName(), dto.getNewAuthorName());
    }

    @PutMapping("/rename-title/{id}")
    public Book updateBookTitle(@PathVariable Long id, @RequestBody BookTitleUpdateDto dto) {
        return updateBookService.updateBookTitle(id, dto);
    }
}
