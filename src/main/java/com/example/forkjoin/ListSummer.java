package com.example.forkjoin;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class ListSummer extends RecursiveTask<Integer> {
    private final List<Integer> listToSum;

    public ListSummer(List<Integer> listToSum) {
        this.listToSum = listToSum;
    }

    @Override
    protected Integer compute() {
        if(listToSum.isEmpty()) {
            log.info("collection is empty.");
            return 0;
        }
        int middleIndex = listToSum.size() / 2;
        System.out.println(String.format("List %s, middle Index: %s", listToSum, middleIndex));
        List<Integer> leftSublist = listToSum.subList(0, middleIndex);
        List<Integer> rightSublist = listToSum.subList(middleIndex + 1, listToSum.size());
        ListSummer leftSummer = new ListSummer(leftSublist);
        ListSummer rightSummer = new ListSummer(rightSublist);

        leftSummer.fork();
        rightSummer.fork();

        Integer leftSum = leftSummer.join();
        Integer rightSum = rightSummer.join();
        var total = leftSum + listToSum.get(middleIndex) + rightSum;
        System.out.println(String.format("Left sum is %s, right sum is %s, total is %s%n", leftSum, rightSum, total));
        return total;
    }
}
