package com.example.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException ignore) {}
            });
            thread.start();
        }
        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long endTime = System.nanoTime();
        return endTime - start;
    }

}
