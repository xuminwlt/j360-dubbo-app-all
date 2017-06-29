package me.j360.dubbo.common.concurrent;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.modules.util.concurrent.threadpool.ThreadPoolUtil;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Package: cn.paomiantv.common.concurrent
 * User: min_xu
 * Date: 2017/6/29 下午4:01
 * 说明：并发容器
 */

@Slf4j
public class DefaultExecutor implements DisposableBean {

    private static ThreadPoolExecutor executor;

    public DefaultExecutor() {
        PriorityBlockingQueue queue = new PriorityBlockingQueue(100);
        RejectedExecutionHandler reh = new ThreadPoolExecutor.CallerRunsPolicy();

        this.executor = new ThreadPoolExecutor(30, 100, 60, TimeUnit.SECONDS, queue, ThreadPoolUtil.buildThreadFactory("DefaultPriorityExecutor", true, (Thread t, Throwable e) -> {
            log.error("优先级任务发生异常:{}", t.toString(), e);
        }), reh);

    }


    //提交Future任务
    public static <T> CompletableFuture<T> supplySync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }


    //执行异步任务
    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }


    //提交Runnable任务
    public static void execute(Runnable command) {
        executor.execute(command);
    }

    //普通submit
    public static <T> Future<T> submit(Callable<T> command) {
        return executor.submit(command);
    }

    @Override
    public void destroy() throws Exception {
        ThreadPoolUtil.gracefulShutdown(this.executor, 3000);
    }
}
