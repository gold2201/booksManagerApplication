package com.bookmanagmentapp.bookmanagmentapplication.dto;

import com.bookmanagmentapp.bookmanagmentapplication.service.LogExportService;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogExportAsyncHelper {
    private LogExportService logExportService;

    @Lazy
    @Autowired
    public void setLogExportService(LogExportService logExportService) {
        this.logExportService = logExportService;
    }

    @Async
    public void asyncExport(LocalDate date, UUID taskId) {
        log.debug("Асинхронный запуск экспорта логов на дату {}", date);
        logExportService.processExport(taskId, date);
    }
}

