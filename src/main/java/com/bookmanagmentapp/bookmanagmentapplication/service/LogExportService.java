package com.bookmanagmentapp.bookmanagmentapplication.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogExportService {
    private final Map<UUID, String> taskStatus = new ConcurrentHashMap<>();
    private final Map<UUID, File> exportedFiles = new ConcurrentHashMap<>();

    public UUID startExport(LocalDate date) {
        UUID taskId = UUID.randomUUID();
        taskStatus.put(taskId, "PROCESSING");

        asyncExport(date, taskId); // Асинхронный вызов
        return taskId;
    }

    @Async
    public void asyncExport(LocalDate date, UUID taskId) {
        processExport(taskId, date);
    }

    private void processExport(UUID taskId, LocalDate date) {
        try {
            String fileName = String.format("logs/app-%s.log", date);
            File logFile = new File(fileName);

            if (!logFile.exists()) {
                taskStatus.put(taskId, "NOT_FOUND");
                return;
            }

            File tempFile = Files.createTempFile("exported-log-", ".log").toFile();
            Files.copy(logFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            exportedFiles.put(taskId, tempFile);
            taskStatus.put(taskId, "DONE");

            log.info("✅ Лог-файл {} экспортирован как {}", fileName, tempFile.getName());
        } catch (IOException e) {
            log.error("❌ Ошибка экспорта логов: {}", e.getMessage(), e);
            taskStatus.put(taskId, "ERROR");
        }
    }

    public String getStatus(UUID taskId) {
        return taskStatus.getOrDefault(taskId, "NOT_FOUND");
    }

    public File getFile(UUID taskId) {
        if (!"DONE".equals(taskStatus.get(taskId))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Файл еще не готов или не найден");
        }

        return exportedFiles.get(taskId);
    }

    @Scheduled(fixedRate = 3600000) // раз в час
    public void cleanUpTempFiles() {
        exportedFiles.forEach((uuid, file) -> {
            try {
                if (file.exists()) {
                    Files.delete(file.toPath());
                    log.info("Удален временный файл: {}", file.getName());
                }
            } catch (IOException e) {
                log.warn("⚠️ Не удалось удалить временный файл {}: {}", file.getName(), e.getMessage());
            }
        });

        exportedFiles.clear();
        taskStatus.entrySet().removeIf(entry -> !"DONE".equals(entry.getValue()));
    }
}

