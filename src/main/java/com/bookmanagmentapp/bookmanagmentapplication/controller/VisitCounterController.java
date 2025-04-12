package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.service.VisitCounterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visits")
public class VisitCounterController {
    private final VisitCounterService visitCounterService;

    public VisitCounterController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Operation(summary = "Получить количество посещений для каждого URL",
            description = "Возвращает счетчики посещений для каждого URL")
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Integer>> getVisitCounts() {
        Map<String, Integer> counts = visitCounterService.getAllCounts();
        return ResponseEntity.ok(counts);
    }

    @Operation(summary = "Получить количество посещений для конкретного URL",
            description = "Возвращает количество посещений для заданного URL")
    @GetMapping("/count")
    public ResponseEntity<Integer> getVisitCount(@RequestParam @NotBlank String url) {
        int count = visitCounterService.getCount(url);
        return ResponseEntity.ok(count);
    }
}

