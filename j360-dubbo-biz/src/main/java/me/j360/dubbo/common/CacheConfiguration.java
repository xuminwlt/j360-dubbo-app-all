package me.j360.dubbo.common;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: min_xu
 * @date: 2018/3/20 下午3:23
 * 说明：
 */

@Configuration
public class CacheConfiguration {


    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setDatabase(0)
                .setConnectionMinimumIdleSize(3)
                .setConnectionPoolSize(5)
                .setIdleConnectionTimeout(3000)
                .setConnectTimeout(3000)
                .setTimeout(3000);
        return Redisson.create(config);
    }

}
