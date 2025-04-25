package com.bookmanagmentapp.bookmanagmentapplication.controller.bookcontrollers;

import com.bookmanagmentapp.bookmanagmentapplication.dto.BookDto;
import com.bookmanagmentapp.bookmanagmentapplication.dto.BookUpdateDto;
import com.bookmanagmentapp.bookmanagmentapplication.service.bookservices.UpdateBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Обновить книгу",
            description = "Обновляет книгу с указанным ID")
    @PutMapping("updateBook/{id}")
    public ResponseEntity<BookDto> updateBookPartial(@PathVariable Long id,
                                                     @RequestBody BookUpdateDto dto) {
        BookDto updated = updateBookService.updateBookPartial(id, dto);
        return ResponseEntity.ok(updated);
    }
}
