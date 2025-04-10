package com.bookmanagmentapp.bookmanagmentapplication.controller.bookcontrollers;

import com.bookmanagmentapp.bookmanagmentapplication.dto.BookAuthorsUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookTitleUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.model.Book;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.UpdateBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Validated
@Tag(name = "Book Update API", description = "Эндпоинты для обновления данных книги")
public class UpdateBookController {
    private final UpdateBookService updateBookService;

    @Operation(summary = "Обновить авторов книги",
            description = "Заменяет старого автора на нового для книги с указанным ID")
    @PutMapping("/update-authors/{id}")
    public ResponseEntity<BookDto> updateAuthors(
            @PathVariable Long id,
            @RequestBody @Valid BookAuthorsUpdateDto dto) {
        Book updatedBook = updateBookService.updateAuthor(id, dto.getOldAuthorName(),
                dto.getNewAuthorName()).toEntity();
        return ResponseEntity.ok(BookDto.fromEntity(updatedBook));
    }

    @Operation(summary = "Обновить заголовок книги",
            description = "Обновляет название книги с указанным ID")
    @PutMapping("/update-title/{id}")
    public ResponseEntity<BookDto> updateBookTitle(
            @PathVariable Long id,
            @RequestBody @Valid BookTitleUpdateDto dto) {
        Book updatedBook = updateBookService.updateBookTitle(id, dto).toEntity();
        return ResponseEntity.ok(BookDto.fromEntity(updatedBook));
    }
}
