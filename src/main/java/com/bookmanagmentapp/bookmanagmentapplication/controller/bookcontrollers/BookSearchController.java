package com.bookmanagmentapp.bookmanagmentapplication.controller.bookcontrollers;

import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.BookSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Validated
@Tag(name = "Book Search API", description = "Эндпоинты для поиска книг")
public class BookSearchController {
    private final BookSearchService bookSearchService;

    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг из базы данных")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookSearchService.getAllBooks();
    }

    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу с указанным ID")
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable @Min(1) Long id) {
        return bookSearchService.getBookById(id);
    }

    @Operation(summary = "Получить книги по имени автора",
            description = "Возвращает список книг, написанных автором с указанным именем")
    @GetMapping("/show-book/author/{authorName}")
    public List<Book> getBooksByAuthorName(@PathVariable @Size(min = 1,
            message = "Имя автора не может быть пустым") String authorName) {
        return bookSearchService.getBooksByAuthorName(authorName);
    }
}

