package com.bookmanagmentapp.bookmanagmentapplication.controller.authorconrtollers;

import com.bookmanagmentapp.bookmanagmentapplication.model.Author;
import com.bookmanagmentapp.bookmanagmentapplication.service.authorservices.AuthorSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
@Validated
@Tag(name = "Author API", description = "Операции для работы с авторами")
public class AuthorSearchController {
    private final AuthorSearchService authorSearchService;

    /* @Operation(summary = "Получить всех авторов", description = "Возвращает список всех авторов")
    @ApiResponse(responseCode = "200", description = "Список авторов успешно получен")
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorSearchService.getAllAuthors();
    } */

    @Operation(summary = "Получить автора по ID", description = "Возвращает автора по указанному ID")
    @ApiResponse(responseCode = "200", description = "Автор найден")
    @ApiResponse(responseCode = "404", description = "Автор не найден")
    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable @Min(1) Long id) {
        return authorSearchService.getAuthorById(id);
    }

    @Operation(summary = "Получить авторов по названию книги",
            description = "Возвращает список авторов, написавших указанную книгу")
    @ApiResponse(responseCode = "200", description = "Список авторов успешно получен")
    @ApiResponse(responseCode = "400", description = "Некорректное название книги")
    @GetMapping("/by-book")
    public List<Author> getAuthorsByBookTitle(@RequestParam @NotBlank(message
            = "Название книги не может быть пустым") String bookTitle) {
        return authorSearchService.getAuthorsByBookTitle(bookTitle);
    }
}

