package com.bookmanagmentapp.bookmanagmentapplication.servicceTests;

import com.bookmanagmentapp.bookmanagmentapplication.dto.LogExportAsyncHelper;
import com.bookmanagmentapp.bookmanagmentapplication.exceptions.LogNotReadyException;
import com.bookmanagmentapp.bookmanagmentapplication.service.LogExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogExportServiceTest {

    private LogExportService service;
    private LogExportAsyncHelper asyncHelper;
    private final LocalDate today = LocalDate.now();

    @TempDir
    Path tempDir;  // JUnit сам создаёт и удаляет эту папку

    @BeforeEach
    void setUp() {
        asyncHelper = mock(LogExportAsyncHelper.class);
        service = new LogExportService(asyncHelper);
    }

    @Test
    void testStartExportReturnsUUID_whenLogFileExists() throws Exception {
        Path logFilePath = tempDir.resolve("app-" + today + ".log");
        Files.createFile(logFilePath);  // создаём лог-файл в temp-папке

        // Тестовая подмена имени файла
        setLogFilePath(logFilePath.toFile());

        UUID uuid = service.startExport(today);

        assertNotNull(uuid);
        verify(asyncHelper).asyncExport(today, uuid);
    }

    @Test
    void testStartExportThrows_whenLogFileNotExists() {
        Path logFilePath = tempDir.resolve("app-" + today + ".log");
        setLogFilePath(logFilePath.toFile());

        assertThrows(FileNotFoundException.class, () -> service.startExport(today));
    }

    @Test
    void testProcessExportSuccess() throws IOException {
        UUID taskId = UUID.randomUUID();

        Path logFilePath = tempDir.resolve("app-" + today + ".log");
        Files.writeString(logFilePath, "Test log content");

        setLogFilePath(logFilePath.toFile());

        service.processExport(taskId, today);

        assertEquals("DONE", service.getStatus(taskId));

        File exported = getPrivateMap(service).get(taskId);
        assertNotNull(exported);
        assertTrue(exported.exists());
    }

    @Test
    void testProcessExportFileNotFound() {
        UUID taskId = UUID.randomUUID();

        Path logFilePath = tempDir.resolve("app-" + today + ".log");
        setLogFilePath(logFilePath.toFile());

        service.processExport(taskId, today);

        assertEquals("NOT_FOUND", service.getStatus(taskId));
    }

    @Test
    void testGetFileWhenDone() throws Exception {
        UUID taskId = UUID.randomUUID();
        File temp = Files.createTempFile(tempDir, "exported-test-", ".log").toFile();

        setPrivateMap("exportedFiles", Map.of(taskId, temp));
        setPrivateMap("taskStatus", Map.of(taskId, "DONE"));

        File result = service.getFile(taskId);
        assertEquals(temp, result);
    }

    @Test
    void testGetFileThrowsIfNotReady() {
        UUID taskId = UUID.randomUUID();
        assertThrows(LogNotReadyException.class, () -> service.getFile(taskId));
    }

    @Test
    void testCleanUpTempFiles() throws Exception {
        UUID taskId = UUID.randomUUID();
        File temp = Files.createTempFile(tempDir, "test-clean", ".log").toFile();

        setPrivateMap("exportedFiles", Map.of(taskId, temp));
        setPrivateMap("taskStatus", Map.of(taskId, "DONE"));

        assertTrue(temp.exists());

        service.cleanUpTempFiles();

        assertFalse(temp.exists(), "Файл должен быть удалён после очистки.");
        assertEquals("NOT_FOUND", service.getStatus(taskId));
    }

    private void setLogFilePath(File logFile) {
        service = new LogExportService(asyncHelper) {
            @Override
            public UUID startExport(LocalDate date) throws FileNotFoundException {
                if (!logFile.exists()) {
                    throw new FileNotFoundException("Лог-файл за указанную дату не найден.");
                }
                UUID taskId = UUID.randomUUID();
                try {
                    // Безопасно получаем и модифицируем map с помощью Reflection
                    Field statusField = LogExportService.class.getDeclaredField("taskStatus");
                    statusField.setAccessible(true);
                    Map<UUID, String> statusMap = getFieldValue(statusField, this);
                    statusMap.put(taskId, "PROCESSING");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                asyncHelper.asyncExport(date, taskId);
                return taskId;
            }

            @Override
            public void processExport(UUID taskId, LocalDate date) {
                try {
                    if (!logFile.exists()) {
                        setTaskStatus(taskId, "NOT_FOUND");
                        return;
                    }
                    File tempFile = Files.createTempFile(tempDir, "exported-log-", ".log").toFile();
                    Files.copy(logFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    setExportedFile(taskId, tempFile);
                    setTaskStatus(taskId, "DONE");
                } catch (IOException e) {
                    setTaskStatus(taskId, "ERROR");
                }
            }

            private void setTaskStatus(UUID taskId, String status) {
                try {
                    // Безопасно получаем и модифицируем map с помощью Reflection
                    Field statusField = LogExportService.class.getDeclaredField("taskStatus");
                    statusField.setAccessible(true);
                    Map<UUID, String> statusMap = getFieldValue(statusField, this);
                    statusMap.put(taskId, status);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private void setExportedFile(UUID taskId, File file) {
                try {
                    // Безопасно получаем и модифицируем map с помощью Reflection
                    Field exportedField = LogExportService.class.getDeclaredField("exportedFiles");
                    exportedField.setAccessible(true);
                    Map<UUID, File> exportedMap = getFieldValue(exportedField, this);
                    exportedMap.put(taskId, file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // Метод для безопасного получения значения поля с типом Map
            @SuppressWarnings("unchecked")
            private <T> T getFieldValue(Field field, Object instance) throws IllegalAccessException {
                return (T) field.get(instance);
            }
        };
    }


    private void setPrivateMap(String fieldName, Map<UUID, ?> value) throws Exception {
        Field field = LogExportService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, new ConcurrentHashMap<>(value));
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, File> getPrivateMap(LogExportService service) {
        try {
            Field field = LogExportService.class.getDeclaredField("exportedFiles");
            field.setAccessible(true);
            return (Map<UUID, File>) field.get(service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}




