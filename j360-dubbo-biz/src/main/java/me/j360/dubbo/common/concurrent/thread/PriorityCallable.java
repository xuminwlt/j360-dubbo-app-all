package me.j360.dubbo.common.concurrent.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;


@Slf4j
public class PriorityCallable<T> implements Callable<T>, Comparable<Long> {

    private long priority;

    private Callable<T> task;

    /**
     * priority值越大优先级越高
     */
    public PriorityCallable(long priority, Callable<T> task) {
        this.priority = priority;
        this.task = task;
        if (this.task == null) {
            throw new IllegalArgumentException("task cannot be null");
        }
    }

    @Override
    public int compareTo(Long o) {
        return o.compareTo(priority);
    }

    @Override
    public T call() throws Exception {
        return task.call();
    }
}