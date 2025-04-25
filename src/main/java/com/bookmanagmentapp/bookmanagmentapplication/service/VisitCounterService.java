package com.bookmanagmentapp.bookmanagmentapplication.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class VisitCounterService {
    private final ConcurrentHashMap<String, AtomicInteger> urlCounters = new ConcurrentHashMap<>();

    public void increment(String url) {
        urlCounters.computeIfAbsent(url, key -> new AtomicInteger(0)).incrementAndGet();
    }

    public int getCount(String url) {
        return urlCounters.getOrDefault(url, new AtomicInteger(0)).get();
    }

    public Map<String, Integer> getAllCounts() {
        Map<String, Integer> counts = new HashMap<>();
        urlCounters.forEach((url, counter) -> counts.put(url, counter.get()));
        return counts;
    }
}
