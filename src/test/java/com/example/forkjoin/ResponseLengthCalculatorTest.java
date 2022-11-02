package com.example.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;

class ResponseLengthCalculatorTest {
    @Test
    void shoudlReturnEmptyMapForEmptyList() {
        ResponseLengthCalculator responseLengthCalculator = new ResponseLengthCalculator(List.of());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(responseLengthCalculator);
        Map<String, Integer> result = responseLengthCalculator.join();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandle200Ok() {
        ResponseLengthCalculator responseLengthCalculator = new ResponseLengthCalculator(List.of("http://httpstat.us/200"));
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(responseLengthCalculator);
        Map<String, Integer> result = responseLengthCalculator.join();
        assertEquals(1, result.size());
        assertTrue(result.containsKey("http://httpstat.us/200"));
        assertTrue(result.containsValue(6));
    }
    @Test
    void shouldFetchResponseForDifferentResponseStatus() {
        ResponseLengthCalculator responseLengthCalculator = new ResponseLengthCalculator(List.of(
                "http://httpstat.us/200",
                "http://httpstat.us/302",
                "http://httpstat.us/404",
                "http://httpstat.us/502"
        ));
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(responseLengthCalculator);
        Map<String, Integer> result = responseLengthCalculator.join();
        assertEquals(4, result.size());
    }
}