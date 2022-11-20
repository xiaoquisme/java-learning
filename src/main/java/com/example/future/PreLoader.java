package com.example.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PreLoader {
    private final FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            System.out.println("load info from remote");
            return "success";
        }
    });
    private final Thread thread = new Thread(futureTask);

    public void start() {
        thread.start();
    }
    public String get() throws InterruptedException {
        try {
            return futureTask.get();
        } catch (ExecutionException e) {
            /*
            无论任何代码跑出异常，都会被封装为ExecutionException
             */
            if(e.getCause() instanceof LoadDataException) {
                // load some data form remote error.
            }
            throw new RuntimeException(e);
        }
    }
}
