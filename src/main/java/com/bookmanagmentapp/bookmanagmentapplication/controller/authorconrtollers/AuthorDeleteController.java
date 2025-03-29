package com.bookmanagmentapp.bookmanagmentapplication.controller.authorconrtollers;

import com.bookmanagmentapp.bookmanagmentapplication.service.authorservices.AuthorDeleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
@Validated
@Tag(name = "Author API", description = "Операции для работы с авторами")
public class AuthorDeleteController {
    private final AuthorDeleteService authorDeleteService;

    @Operation(summary = "Удалить автора по ID", description = "Удаляет автора по указанному ID")
    @ApiResponse(responseCode = "204", description = "Автор успешно удалён")
    @ApiResponse(responseCode = "404", description = "Автор не найден")
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable @Min(1) Long id) {
        authorDeleteService.deleteAuthor(id);
    }
}
