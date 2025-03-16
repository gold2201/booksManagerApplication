package com.bookmanagmentapp.bookmanagmentapplication.controller;

import com.bookmanagmentapp.bookmanagmentapplication.InMemoryCache;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CacheController {
    private final InMemoryCache<String, Object> cache;

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCache() {
        cache.clear();
        return ResponseEntity.ok("Кэш очищен");
    }
}
