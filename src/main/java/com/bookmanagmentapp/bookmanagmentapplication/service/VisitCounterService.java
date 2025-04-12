package com.bookmanagmentapp.bookmanagmentapplication.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class VisitCounterService {
    // Счетчики для каждого URL
    private final ConcurrentHashMap<String, AtomicInteger> urlCounters = new ConcurrentHashMap<>();

    // Метод увеличения счетчика для заданного URL (синхронизирован через AtomicInteger)
    public void increment(String url) {
        urlCounters.computeIfAbsent(url, key -> new AtomicInteger(0)).incrementAndGet();
    }

    // Метод для получения текущего счетчика по URL
    public int getCount(String url) {
        return urlCounters.getOrDefault(url, new AtomicInteger(0)).get();
    }

    // Метод для получения всех счетчиков (например, для отчета)
    public Map<String, Integer> getAllCounts() {
        Map<String, Integer> counts = new HashMap<>();
        urlCounters.forEach((url, counter) -> counts.put(url, counter.get()));
        return counts;
    }
}
