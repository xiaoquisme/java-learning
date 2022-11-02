package com.example.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListSummerTest {


    @Test
    void shoudSumEmptyList() {
        ListSummer summer = new ListSummer(List.of());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(summer);
        Integer result = summer.join();
        assertEquals(0, result);
    }

    @Test
    void shouldSumListOneElement() {
        ListSummer summer = new ListSummer(List.of(1));
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(summer);
        Integer result = summer.join();
        assertEquals(1, result);
    }

    @Test
    void shoudlSumListWithMultipleElemnts() {
        ListSummer summer = new ListSummer(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(summer);
        Integer result = summer.join();
        assertEquals(45,result);
    }
}