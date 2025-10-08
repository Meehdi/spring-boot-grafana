package com.meehdi.demo.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CustomMetricsConfig {

    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final AtomicInteger queueSize = new AtomicInteger(0);

    @Bean
    public MeterBinder customMetrics() {
        return (MeterRegistry registry) -> {
            // Gauge for active users (simulated)
            Gauge.builder("app.active.users", activeUsers, AtomicInteger::get)
                    .description("Number of active users")
                    .tag("type", "business")
                    .register(registry);

            // Gauge for queue size (simulated)
            Gauge.builder("app.queue.size", queueSize, AtomicInteger::get)
                    .description("Current queue size")
                    .tag("type", "technical")
                    .register(registry);

            // Memory usage gauge
            Gauge.builder("app.memory.used.percent",
                            Runtime.getRuntime(),
                            runtime -> (runtime.totalMemory() - runtime.freeMemory()) * 100.0 / runtime.maxMemory())
                    .description("Memory usage percentage")
                    .tag("type", "system")
                    .register(registry);
        };
    }

    // Simulate fluctuating metrics
    @Bean
    public MetricsSimulator metricsSimulator() {
        return new MetricsSimulator(activeUsers, queueSize);
    }

    public static class MetricsSimulator {
        private final AtomicInteger activeUsers;
        private final AtomicInteger queueSize;

        public MetricsSimulator(AtomicInteger activeUsers, AtomicInteger queueSize) {
            this.activeUsers = activeUsers;
            this.queueSize = queueSize;
            startSimulation();
        }

        private void startSimulation() {
            // Simulate active users fluctuation
            new Thread(() -> {
                while (true) {
                    try {
                        int change = (int) (Math.random() * 10) - 5;
                        int newValue = Math.max(0, Math.min(100, activeUsers.get() + change));
                        activeUsers.set(newValue);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }).start();

            // Simulate queue size fluctuation
            new Thread(() -> {
                while (true) {
                    try {
                        int change = (int) (Math.random() * 20) - 10;
                        int newValue = Math.max(0, Math.min(500, queueSize.get() + change));
                        queueSize.set(newValue);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }).start();
        }
    }
}