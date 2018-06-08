package me.j360.dubbo.apollo.annotation;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.apollo.common.CacheConfiguration;
import me.j360.dubbo.apollo.repository.UserRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * Package: cn.paomaintv.user.bootstrap
 * User: min_xu
 * Date: 2017/6/12 下午7:56
 * 说明：
 */

@Slf4j
public class ApplicationBootstrap {


    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        context.start();

        UserRepository userRepository = (UserRepository) context.getBean("userRepository");
        CacheConfiguration cacheConfiguration = (CacheConfiguration) context.getBean("cacheConfiguration");

        while (true) {
            String name = userRepository.getUserCacheable(1L);
            System.out.println(String.format("name=%s", name));

            System.out.println(String.format("timeout=%s", cacheConfiguration.getTimeout()));
            TimeUnit.SECONDS.sleep(5);
        }

    }
}
