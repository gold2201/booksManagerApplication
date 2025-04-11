package com.bookmanagmentapp.bookmanagmentapplication.logtests;

import com.bookmanagmentapp.bookmanagmentapplication.service.LogExportService;
import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class LogExportServiceTest {

    private LogExportService service;
    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        service = new LogExportService();
    }

    @Test
    void testStartExportReturnsUUID() {
        UUID uuid = service.startExport(today);
        assertNotNull(uuid);
        String status = service.getStatus(uuid);
        assertTrue(List.of("PROCESSING", "DONE", "ERROR", "NOT_FOUND").contains(status));
    }

    @Test
    void testExportAndGetFileSuccess() throws Exception {
        String fakeLogPath = "logs/app-" + today + ".log";
        File fakeLog = new File(fakeLogPath);
        File parentDir = fakeLog.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Не удалось создать директорию для логов: " + parentDir);
        }
        Files.writeString(fakeLog.toPath(), "Test log line");

        UUID uuid = service.startExport(today);

        await().atMost(Duration.ofSeconds(2))
                .until(() -> "DONE".equals(service.getStatus(uuid)));

        File exported = service.getFile(uuid);
        assertTrue(exported.exists());
    }

    @Test
    void testGetFileThrowsIfNotReady() {
        UUID uuid = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> service.getFile(uuid));
    }

    @Test
    void testCleanUpTempFiles() throws Exception {
        File temp = Files.createTempFile("test-clean", ".log").toFile();
        UUID uuid = UUID.randomUUID();

        setPrivateMap("exportedFiles", Map.of(uuid, temp));
        setPrivateMap("taskStatus", Map.of(uuid, "DONE"));

        assertTrue(temp.exists());

        service.cleanUpTempFiles();

        assertFalse(temp.exists(), "Файл должен быть удалён");
        assertEquals("NOT_FOUND", service.getStatus(uuid)); // ✅ теперь работает
    }


    private void setPrivateMap(String fieldName, Map<UUID, ?> value) throws Exception {
        Field field = LogExportService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, new ConcurrentHashMap<>(value));
    }
}

