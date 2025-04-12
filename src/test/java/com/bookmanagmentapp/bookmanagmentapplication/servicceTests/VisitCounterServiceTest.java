package com.bookmanagmentapp.bookmanagmentapplication.servicceTests;

import com.bookmanagmentapp.bookmanagmentapplication.service.VisitCounterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class VisitCounterServiceTest {

    private VisitCounterService service;

    @BeforeEach
    void setUp() {
        service = new VisitCounterService();
    }

    @Test
    void testIncrementAndGetCount() {
        String url = "/test";

        assertEquals(0, service.getCount(url), "Изначально счетчик должен быть 0");

        service.increment(url);
        assertEquals(1, service.getCount(url), "После одного инкремента счетчик должен быть 1");

        service.increment(url);
        service.increment(url);
        assertEquals(3, service.getCount(url), "После трех инкрементов счетчик должен быть 3");
    }

    @Test
    void testGetAllCounts() {
        service.increment("/url1");
        service.increment("/url1");
        service.increment("/url2");

        Map<String, Integer> allCounts = service.getAllCounts();

        assertEquals(2, allCounts.get("/url1"));
        assertEquals(1, allCounts.get("/url2"));
        assertEquals(2, allCounts.size(), "Должно быть два URL в карте");
    }

    @Test
    void testCountForUnknownUrl() {
        String unknownUrl = "/unknown";
        assertEquals(0, service.getCount(unknownUrl), "Для неинициализированного URL счетчик должен быть 0");
    }

    @Test
    void testThreadSafety() throws InterruptedException {
        String url = "/thread-safe";
        int threads = 10;
        int incrementsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    service.increment(url);
                }
            });
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        assertTrue(finished, "Потоки не завершились вовремя!");

        assertEquals(threads * incrementsPerThread, service.getCount(url),
                "Счетчик должен корректно учитывать инкременты из всех потоков");
    }
}

