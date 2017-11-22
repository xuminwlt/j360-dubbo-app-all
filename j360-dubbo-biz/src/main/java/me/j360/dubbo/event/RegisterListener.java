package me.j360.dubbo.event;

import com.google.common.eventbus.Subscribe;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.common.concurrent.DefaultAsyncEventBus;
import me.j360.dubbo.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: min_xu
 * @date: 2017/11/22 下午6:22
 * 说明：
 */

@Component
@Lazy(value = false)
public class RegisterListener {

    private DefaultAsyncEventBus eventBus;
    private UserManager userManager;

    @PostConstruct
    public void init() {
        this.eventBus.register(this);
    }

    @Autowired
    public RegisterListener(DefaultAsyncEventBus defaultAsyncEventBus, UserManager userManager) {
        this.eventBus = defaultAsyncEventBus;
        this.userManager = userManager;
    }

    @Subscribe
    public void handler(RegisterEvent event) {
        Long uid = event.getUid();
        //sample
        userManager.insert(new UserDTO());
    }

}
