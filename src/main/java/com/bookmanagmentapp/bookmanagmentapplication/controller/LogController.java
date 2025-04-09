package com.bookmanagmentapp.bookmanagmentapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/logs")
@Validated
@Tag(name = "Log API", description = "Операции для работы с логами приложения")
public class LogController {
    private static final String LOG_DIR = "logs/";

    @Operation(summary = "Получить лог-файл", description = "Возвращает лог-файл за указанную дату")
    @ApiResponse(responseCode = "200", description = "Лог-файл успешно загружен")
    @ApiResponse(responseCode = "404", description = "Лог-файл не найден")
    @GetMapping("/{date}")
    public ResponseEntity<Resource> getLogFile(@PathVariable String date) {
        Path logFilePath = Paths.get(LOG_DIR + "app-" + date + ".log");

        if (!Files.exists(logFilePath)) {
            logFilePath = Paths.get(LOG_DIR + "app.log");

            if (!Files.exists(logFilePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лог-файлы отсутствуют");
            }
        }

        Resource fileResource = new FileSystemResource(logFilePath);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + logFilePath.getFileName())
                .body(fileResource);
    }
}