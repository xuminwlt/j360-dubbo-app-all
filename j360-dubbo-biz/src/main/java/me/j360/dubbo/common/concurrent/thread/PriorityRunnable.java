package me.j360.dubbo.common.concurrent.thread;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PriorityRunnable implements Runnable, Comparable<Long> {

    private long priority;

    private Runnable task;

    /**
     * priority值越大优先级越高
     */
    public PriorityRunnable(long priority, Runnable task) {
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
    public void run() {
        task.run();
    }
}