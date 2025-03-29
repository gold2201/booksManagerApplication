package com.bookmanagmentapp.bookmanagmentapplication.controller.bookcontrollers;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.CreateDeleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Validated
@Tag(name = "Book Creat/Delete API", description = "Эндпоинты для создания и удаления книг")
public class CreateDeleteController {
    private final CreateDeleteService createDeleteService;

    @Operation(summary = "Создать книгу", description = "Создает новую книгу с указанными данными")
    @PostMapping("/create-book")
    public Book createBook(@RequestBody @Valid Book book) {
        return createDeleteService.saveBook(book);
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по указанному ID")
    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable @Min(1) Long id) {
        createDeleteService.deleteBook(id);
    }
}
