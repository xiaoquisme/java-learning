package com.example.forkjoin;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ResponseLengthCalculator extends RecursiveTask<Map<String, Integer>> {
    private final List<String> links;

    public ResponseLengthCalculator(List<String> links) {
        this.links = links;
    }

    @Override
    protected Map<String, Integer> compute() {
        if (links.isEmpty()) {
            return Collections.emptyMap();
        }
        int middle = links.size() / 2;
        ResponseLengthCalculator leftPartition = new ResponseLengthCalculator(links.subList(0, middle));
        ResponseLengthCalculator rightPartition = new ResponseLengthCalculator(links.subList(middle + 1, links.size()));
        leftPartition.fork();
        rightPartition.fork();

        String middleLink = links.get(middle);
        HttpRequester httpRequester = new HttpRequester(middleLink);
        String response;
        try {
            ForkJoinPool.managedBlock(httpRequester);
            response = httpRequester.response;
        } catch (InterruptedException ex) {
            response = "";
        }
        HashMap<String, Integer> responseMap = new HashMap<>(links.size());
        Map<String, Integer> leftLinks = leftPartition.join();
        responseMap.putAll(leftLinks);
        responseMap.put(middleLink, response.length());
        Map<String, Integer> rightLinks = rightPartition.join();
        responseMap.putAll(rightLinks);
        return responseMap;
    }

    public static class HttpRequester implements ForkJoinPool.ManagedBlocker {
        private final String link;
        private String response;

        public HttpRequester(String link) {
            this.link = link;
        }

        @Override
        public boolean block() throws InterruptedException {
            HttpGet headRequest = new HttpGet(link);
            CloseableHttpClient client = HttpClientBuilder.create()
                    .disableRedirectHandling()
                    .build();
            try {
                CloseableHttpResponse response = client.execute(headRequest);
                this.response = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                this.response = "";
            }
            return true;
        }

        @Override
        public boolean isReleasable() {
            return false;
        }
    }
}
