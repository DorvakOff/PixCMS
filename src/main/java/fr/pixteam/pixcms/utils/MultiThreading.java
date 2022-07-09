package fr.pixteam.pixcms.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.constraints.NotNull;

public class MultiThreading {

    private static final @NotNull
    ExecutorService POOL = Executors.newFixedThreadPool(250, new ThreadFactory() {
        final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public @NotNull Thread newThread(@NotNull Runnable r) {
            return new Thread(r, String.format("Thread %s", counter.incrementAndGet()));
        }
    });

    private static final @NotNull
    ScheduledExecutorService RUNNABLE_POOL = Executors.newScheduledThreadPool(100,
            new ThreadFactory() {
                private final AtomicInteger counter = new AtomicInteger(0);

                @Override
                public @NotNull Thread newThread(@NotNull Runnable r) {
                    return new Thread(r, String.format("Thread %s", counter.incrementAndGet()));
                }
            });

    public static @NotNull
    ScheduledFuture<?> schedule(@NotNull Runnable r, long initialDelay, long delay, @NotNull TimeUnit unit) {
        return RUNNABLE_POOL.scheduleAtFixedRate(r, initialDelay, delay, unit);
    }

    public static @NotNull ScheduledFuture<?> schedule(@NotNull Runnable r, long delay, @NotNull TimeUnit unit) {
        return RUNNABLE_POOL.schedule(r, delay, unit);
    }

    public static void runAsync(@NotNull Runnable runnable) {
        POOL.execute(runnable);
    }

    public static void shutdown() {
        RUNNABLE_POOL.shutdown();
        POOL.shutdown();
    }
}