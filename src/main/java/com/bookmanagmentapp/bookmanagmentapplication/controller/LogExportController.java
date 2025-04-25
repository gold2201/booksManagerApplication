package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.service.LogExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
@Tag(name = "Логирование", description = "Асинхронная выгрузка логов")
public class LogExportController {
    private final LogExportService logExportService;

    @PostMapping("/export")
    public ResponseEntity<?> exportLogs(@RequestParam @DateTimeFormat
            (iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            UUID taskId = logExportService.startExport(date);
            return ResponseEntity.accepted().body(taskId);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лог-файл за указанную дату не найден");
        }
    }

    @Operation(summary = "Получить статус выгрузки логов")
    @GetMapping("/status/{taskId}")
    public ResponseEntity<String> getStatus(@PathVariable UUID taskId) {
        return ResponseEntity.ok(logExportService.getStatus(taskId));
    }

    @GetMapping("/download/{taskId}")
    public ResponseEntity<?> downloadLog(@PathVariable UUID taskId) {
        Optional<File> optionalFile = logExportService.getFile(taskId);

        if (optionalFile.isEmpty()) {
            String status = logExportService.getStatus(taskId);
            if ("PROCESSING".equals(status)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Файл ещё обрабатывается. Попробуйте позже.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Файл не найден или не существует.");
            }
        }

        File file = optionalFile.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
    }
}

