package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.cache.InMemoryCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
@Tag(name = "Cache API", description = "Операции для управления кэшем")
public class CacheController {
    private final InMemoryCache<String, Object> cache;

    @Operation(summary = "Очистить кэш", description = "Удаляет все данные из кэша")
    @ApiResponse(responseCode = "200", description = "Кэш успешно очищен")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCache() {
        cache.clear();
        return ResponseEntity.ok("Кэш очищен");
    }
}
