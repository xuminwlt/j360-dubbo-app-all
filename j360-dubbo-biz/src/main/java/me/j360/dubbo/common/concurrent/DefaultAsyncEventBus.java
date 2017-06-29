package me.j360.dubbo.common.concurrent;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.modules.util.concurrent.threadpool.ThreadPoolUtil;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Package: cn.paomiantv.common.thread
 * User: min_xu
 * Date: 2017/6/29 下午2:41
 * 说明：异步消息总线
 *
 * 使用案例
 *  1. Service
 *
 *  @Autowire
 *  DefaultAsyncEventBus
 *
 *  2. Listener
 *
 *  private DefaultAsyncEventBus eventBus;
    private Ks3Client client;
    private Ks3TaskService ks3TaskService;

 @Autowired
 public NotifyListener(Ks3EventBus eventBus, Ks3TaskService ks3TaskService) {
 this.eventBus = eventBus;
 this.ks3TaskService = ks3TaskService;
 }

 @PostConstruct
 public void init() {
 this.eventBus.register(this);
 }

 *
 */

@Slf4j
public class DefaultAsyncEventBus implements DisposableBean {

    private final AsyncEventBus eventBus;
    private final BlockingQueue blockingQueue;
    private final ThreadPoolExecutor executor;

    public DefaultAsyncEventBus() {
        this.blockingQueue = new LinkedBlockingQueue<>(1000);
        this.executor = new ThreadPoolExecutor(30, 100, 3, TimeUnit.SECONDS, blockingQueue, ThreadPoolUtil.buildThreadFactory("DefaultAsyncEventBus", true), new ThreadPoolExecutor.AbortPolicy());

        this.eventBus = new AsyncEventBus(executor, new SubscriberExceptionHandler() {
            @Override
            public void handleException(Throwable exception, SubscriberExceptionContext context) {
                log.error("异步消息队列异常: [subscribeMethod={}, event={} ]",context.getSubscriberMethod(), context.getEvent().toString(),exception);
            }
        });

    }

    /**
     * 注册事件
     */
    public void register(Object object){
        eventBus.register(object);
    }

    /**
     * 执行事件
     * @param object
     */
    public void post(Object object){
        eventBus.post(object);
    }

    /**
     * 卸载事件
     * @param object
     */
    public void unRegister(Object object){
        eventBus.unregister(object);
    }


    @Override
    public void destroy() throws Exception {
        ThreadPoolUtil.gracefulShutdown(executor, 3000);
    }
}
