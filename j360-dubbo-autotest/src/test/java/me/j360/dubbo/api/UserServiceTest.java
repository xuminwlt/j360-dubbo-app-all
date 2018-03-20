package me.j360.dubbo.api;


import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.model.result.UserResult;
import me.j360.dubbo.api.service.UserService;
import me.j360.dubbo.base.model.result.DefaultResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;


@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/user-service-consumer.xml"})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void userTest() {
        DefaultResult<UserResult> result = userService.listFriends(1L);
        Set<Long> friends = result.getData().getFriends();

        log.info("friends : {}", friends);
    }
}
