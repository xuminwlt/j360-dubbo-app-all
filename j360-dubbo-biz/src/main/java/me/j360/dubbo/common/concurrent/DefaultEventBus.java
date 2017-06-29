package me.j360.dubbo.common.concurrent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.base.exception.ServiceException;

/**
 * Package: cn.paomiantv.common.thread
 * User: min_xu
 * Date: 2017/6/29 下午2:41
 * 说明： 同步消息总线
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
public class DefaultEventBus {

    private final EventBus eventBus;

    public DefaultEventBus() {
        this.eventBus = new EventBus(new SubscriberExceptionHandler() {
            @Override
            public void handleException(Throwable exception, SubscriberExceptionContext context) {
                log.error("同步消息总线异常: [subscribeMethod={}, event={} ]",context.getSubscriberMethod(),context.getEvent().toString(),exception);
                throw new ServiceException(ErrorCode.BUS_ERROR.getErrorCode(), ErrorCode.BUS_ERROR.getErrorMsg(), exception);
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

}
