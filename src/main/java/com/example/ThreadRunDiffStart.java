package com.example;

import lombok.extern.slf4j.Slf4j;


public class ThreadRunDiffStart {
    /*
    the result:
    main thread name: main
    thread name run: main
    thread name start: Thread-1
     */
    public static void main(String[] args) {
        System.out.println(String.format("main thread name: %s", Thread.currentThread().getName()));
        getThreadNameWithRun();
        getThreadNameWithStart();
    }


    static void getThreadNameWithRun() {
        new Thread(() -> {
            System.out.println(String.format("thread name run: %s", Thread.currentThread().getName()));
        }).run();
    }

    static void getThreadNameWithStart() {
        new Thread(() -> {
            System.out.println(String.format("thread name start: %s", Thread.currentThread().getName()));
        }).start();
    }
}
