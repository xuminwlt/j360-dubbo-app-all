package me.j360.dubbo.redission.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${redisson.address}")
    private String address;

    @Value("${redisson.database}")
    private Integer database;

    @Value("${redisson.connectionMinimumIdleSize}")
    private Integer connectionMinimumIdleSize;

    @Value("${redisson.connectionPoolSize}")
    private Integer connectionPoolSize;

    @Value("${redisson.idleConnectionTimeout}")
    private Integer idleConnectionTimeout;

    @Value("${redisson.connectTimeout}")
    private Integer connectTimeout;

    @Value("${redisson.timeout}")
    private Integer timeout;

    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(database)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setConnectionPoolSize(connectionPoolSize)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setConnectTimeout(connectTimeout)
                .setTimeout(timeout);
        return Redisson.create(config);
    }


    /*@Bean(name = "redissonClient",destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.useReplicatedServers()
                .addNodeAddress(StringUtils.split(address,","))
                .setDatabase(database)
                //.setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                //.setConnectionPoolSize(connectionPoolSize)
                .setMasterConnectionPoolSize(connectionPoolSize)
                .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setConnectTimeout(connectTimeout)
                .setTimeout(timeout);
        return Redisson.create(config);
    }*/

    /*@Bean(name="redissonString",destroyMethod = "shutdown")
    public RedissonClient redissonString() {
        Config config = new Config();
        config.useReplicatedServers()
                .addNodeAddress(StringUtils.split(address,","))
                .setDatabase(database)
                //.setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                //.setConnectionPoolSize(connectionPoolSize)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setConnectTimeout(connectTimeout)
                .setTimeout(timeout);
        return Redisson.create(config);
    }

    @Bean(name="redissonLong",destroyMethod = "shutdown")
    public RedissonClient redissonLong() {
        Config config = new Config();
        config.setCodec(new LongCodec());
        config.useReplicatedServers()
                .addNodeAddress(StringUtils.split(address,","))
                .setDatabase(database)
                //.setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                //.setConnectionPoolSize(connectionPoolSize)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setConnectTimeout(connectTimeout)
                .setTimeout(timeout);
        return Redisson.create(config);
    }*/


}
