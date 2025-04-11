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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogExportService {
    private final Map<UUID, String> taskStatus = new ConcurrentHashMap<>();
    private final Map<UUID, File> exportedFiles = new ConcurrentHashMap<>();

    public UUID startExport(LocalDate date) {
        UUID taskId = UUID.randomUUID();
        taskStatus.put(taskId, "PROCESSING");
        asyncExport(date, taskId); // вызов в том же потоке
        return taskId;
    }

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
        } catch (IOException e) {
            taskStatus.put(taskId, "ERROR");
        }
    }

    public String getStatus(UUID taskId) {
        return taskStatus.getOrDefault(taskId, "NOT_FOUND");
    }

    public File getFile(UUID taskId) {
        if (!"DONE".equals(taskStatus.get(taskId))) {
            throw new RuntimeException("Файл ещё не готов или не найден");
        }
        return exportedFiles.get(taskId);
    }

    public void cleanUpTempFiles() {
        exportedFiles.forEach((uuid, file) -> {
            try {
                if (file.exists()) {
                    Files.delete(file.toPath());
                }
            } catch (IOException e) {
                System.err.println("⚠️ Ошибка удаления временного файла: " + file.getName());
            }
            taskStatus.remove(uuid);
        });
        exportedFiles.clear();
    }
}

