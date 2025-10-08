package com.meehdi.demo.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class MetricsController {

    private final Counter successCounter;
    private final Counter errorCounter;
    private final Counter requestCounter;
    private final Timer requestTimer;
    private final Random random = new Random();

    public MetricsController(MeterRegistry meterRegistry) {
        this.successCounter = Counter.builder("api.requests.success")
                .description("Total successful API requests")
                .tag("endpoint", "demo")
                .register(meterRegistry);

        this.errorCounter = Counter.builder("api.requests.error")
                .description("Total failed API requests")
                .tag("endpoint", "demo")
                .register(meterRegistry);

        this.requestCounter = Counter.builder("api.requests.total")
                .description("Total API requests")
                .register(meterRegistry);

        this.requestTimer = Timer.builder("api.request.duration")
                .description("API request duration")
                .tag("endpoint", "demo")
                .register(meterRegistry);
    }

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        return requestTimer.record(() -> {
            requestCounter.increment();
            simulateProcessing();
            successCounter.increment();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Hello from Spring Boot with Grafana!");
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.ok(response);
        });
    }

    @GetMapping("/random")
    public ResponseEntity<Map<String, Object>> randomEndpoint() {
        return requestTimer.record(() -> {
            requestCounter.increment();
            simulateProcessing();

            // Randomly return success or error
            if (random.nextInt(10) < 7) {
                successCounter.increment();
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("value", random.nextInt(100));
                response.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(response);
            } else {
                errorCounter.increment();
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Random error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        });
    }

    @GetMapping("/slow")
    public ResponseEntity<Map<String, String>> slowEndpoint() {
        return requestTimer.record(() -> {
            requestCounter.increment();

            // Simulate slow processing (1-3 seconds)
            try {
                TimeUnit.MILLISECONDS.sleep(1000 + random.nextInt(2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            successCounter.increment();
            Map<String, String> response = new HashMap<>();
            response.put("message", "This was a slow endpoint");
            response.put("timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.ok(response);
        });
    }

    @PostMapping("/data")
    public ResponseEntity<Map<String, Object>> postData(@RequestBody Map<String, Object> data) {
        return requestTimer.record(() -> {
            requestCounter.increment();
            simulateProcessing();
            successCounter.increment();

            Map<String, Object> response = new HashMap<>();
            response.put("received", data);
            response.put("processed", true);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        });
    }

    @GetMapping("/error")
    public ResponseEntity<Map<String, String>> errorEndpoint() {
        requestCounter.increment();
        errorCounter.increment();

        Map<String, String> response = new HashMap<>();
        response.put("error", "This endpoint always returns an error");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/metrics/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", requestCounter.count());
        stats.put("successfulRequests", successCounter.count());
        stats.put("failedRequests", errorCounter.count());
        stats.put("averageResponseTime", requestTimer.mean(TimeUnit.MILLISECONDS) + "ms");
        stats.put("maxResponseTime", requestTimer.max(TimeUnit.MILLISECONDS) + "ms");

        return ResponseEntity.ok(stats);
    }

    private void simulateProcessing() {
        try {
            // Simulate processing time between 50-200ms
            TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(150));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}