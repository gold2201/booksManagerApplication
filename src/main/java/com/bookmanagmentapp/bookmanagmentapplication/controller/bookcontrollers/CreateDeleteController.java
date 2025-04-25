package com.bookmanagmentapp.bookmanagmentapplication.controller.bookcontrollers;

import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.CreateBookDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.CreateDeleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/create")
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid CreateBookDto bookDto) {
        // Дополнительная валидация для title и authors
        if (bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (bookDto.getAuthors() == null || bookDto.getAuthors().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Book savedBook = createDeleteService.saveBook(bookDto.toEntity());
        return ResponseEntity.ok(BookDto.fromEntity(savedBook));
    }

    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по указанному ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable @Min(1) Long id) {
        createDeleteService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Создать несколько книг", description = "Создает несколько книг одним запросом")
    @PostMapping("/create-books")
    public List<Book> createBooks(@RequestBody @Valid List<Book> books) {
        return createDeleteService.saveBooks(books);
    }
}
