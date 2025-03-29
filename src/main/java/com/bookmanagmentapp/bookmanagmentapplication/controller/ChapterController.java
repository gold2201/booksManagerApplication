package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.model.Chapter;
import com.bookmanagmentapp.bookmanagmentapplication.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chapters")
@Validated
@AllArgsConstructor
@Tag(name = "Chapter API", description = "Операции для работы с главами книг")
public class ChapterController {
    private final ChapterService chapterService;

    @Operation(summary = "Получить главу по ID", description = "Возвращает главу книги по указанному ID")
    @ApiResponse(responseCode = "200", description = "Глава найдена")
    @ApiResponse(responseCode = "404", description = "Глава не найдена")
    @GetMapping("/{id}")
    public Chapter getChapterById(@PathVariable @Min(1) Long id) {
        return chapterService.getChapterById(id);
    }

    @Operation(summary = "Получить все главы", description = "Возвращает список всех глав")
    @ApiResponse(responseCode = "200", description = "Список глав успешно получен")
    @GetMapping
    public List<Chapter> getAllChapters() {
        return chapterService.getAllChapters();
    }

    @Operation(summary = "Обновить главу", description = "Обновляет существующую главу по ID")
    @ApiResponse(responseCode = "200", description = "Глава успешно обновлена")
    @ApiResponse(responseCode = "400", description = "Некорректные данные главы")
    @ApiResponse(responseCode = "404", description = "Глава не найдена")
    @PutMapping("/{id}")
    public Chapter updateChapter(@PathVariable @Min(1) Long id, @RequestBody @Valid Chapter chapter) {
        return chapterService.updateChapter(id, chapter);
    }

    @Operation(summary = "Удалить главу", description = "Удаляет главу книги по указанному ID")
    @ApiResponse(responseCode = "204", description = "Глава успешно удалена")
    @ApiResponse(responseCode = "404", description = "Глава не найдена")
    @DeleteMapping("/{id}")
    public void deleteChapter(@PathVariable @Min(1) Long id) {
        chapterService.deleteChapter(id);
    }
}

