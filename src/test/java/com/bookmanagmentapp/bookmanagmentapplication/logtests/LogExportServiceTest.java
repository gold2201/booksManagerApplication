package com.bookmanagmentapp.bookmanagmentapplication.logtests;

import com.bookmanagmentapp.bookmanagmentapplication.service.LogExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LogExportServiceTest {

    private LogExportService service;

    @BeforeEach
    void setUp() {
        service = new LogExportService();
    }

    @Test
    void testExportEventuallyCompletes() throws InterruptedException {
        UUID uuid = service.startExport(LocalDate.now());
        assertNotNull(uuid);

        // Подождать, пока не станет DONE или не выйдет таймаут
        for (int i = 0; i < 10; i++) {
            String status = service.getStatus(uuid);
            if ("DONE".equals(status)) {
                return; // ОК, экспорт завершен
            }
            Thread.sleep(200); // ждем 200мс
        }

        fail("Экспорт не завершился в течение ожидаемого времени");
    }

    @Test
    void testGetStatusReturnsDefaultIfNotExists() {
        UUID unknown = UUID.randomUUID();
        assertEquals("NOT_FOUND", service.getStatus(unknown));
    }
}
