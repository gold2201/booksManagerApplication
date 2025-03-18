package com.bookmanagmentapp.bookmanagmentapplication.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class InMemoryCache<K, V> {
    private final Map<K, V> cache = new ConcurrentHashMap<>();
    private final Map<K, Long> expirationTimes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final long ttlMillis;

    public InMemoryCache() {
        this.ttlMillis = 300000;
    }

    public void put(K key, V value) {
        cache.put(key, value);
        expirationTimes.put(key, System.currentTimeMillis() + ttlMillis);
        scheduler.schedule(() -> {
            cache.remove(key);
            expirationTimes.remove(key);
        }, ttlMillis, TimeUnit.MILLISECONDS);
    }

    public V get(K key) {
        Long expirationTime = expirationTimes.get(key);
        if (expirationTime != null && System.currentTimeMillis() > expirationTime) {
            cache.remove(key);
            expirationTimes.remove(key);
            return null;
        }
        return cache.get(key);
    }

    public void clear() {
        cache.clear();
        expirationTimes.clear();
    }
}
