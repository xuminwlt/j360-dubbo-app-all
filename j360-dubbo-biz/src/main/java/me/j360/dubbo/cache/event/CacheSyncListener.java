package me.j360.dubbo.cache.event;

import com.google.common.eventbus.Subscribe;
import me.j360.dubbo.cache.CacheSyncManager;
import me.j360.dubbo.common.concurrent.DefaultAsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: min_xu
 * @date: 2017/11/28 下午4:16
 * 说明：
 */

@Component
@Lazy(value = false)
public class CacheSyncListener {

    private DefaultAsyncEventBus eventBus;
    private CacheSyncManager cacheSyncManager;

    @PostConstruct
    public void init() {
        this.eventBus.register(this);
    }

    @Autowired
    public CacheSyncListener(DefaultAsyncEventBus defaultAsyncEventBus, CacheSyncManager cacheSyncManager) {
        this.eventBus = defaultAsyncEventBus;
        this.cacheSyncManager = cacheSyncManager;
    }

    @Subscribe
    public void handler(CacheSyncEvent event) {
        cacheSyncManager.writeStepCount(event);
    }


}
