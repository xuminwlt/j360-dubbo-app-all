package me.j360.dubbo.apollo.repository;

import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.base.constant.AppConfig;
import me.j360.dubbo.base.constant.UserKeys;
import me.j360.dubbo.base.exception.RepositoryException;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Package: me.j360.dubbo.dao.repository
 * User: min_xu
 * Date: 16/8/23 下午2:57
 * 说明：
 */

@Repository
public class UserRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedissonClient redissonClient;


    /**
     * redission 作为二级缓存DAO层的案例
     * @param id
     * @return
     */
    public String getUserCacheable(long id) {
        try{
            RBucket<String> rBucket = redissonClient.getBucket(String.format(UserKeys.USER_DO_ID, id, JsonJacksonCodec.INSTANCE));
            if (rBucket.isExists()) {
                rBucket.expire(AppConfig.COMMON_CACHE_DAYS, TimeUnit.MINUTES);
                return rBucket.get();
            }
            String name = jdbcTemplate.queryForObject("select username from user where id = ?", String.class, new Object[]{id});
            if (Objects.nonNull(name)) {
                redissonClient.getBucket(String.format(UserKeys.USER_DO_ID, id),JsonJacksonCodec.INSTANCE)
                        .setAsync(name, AppConfig.COMMON_CACHE_DAYS, TimeUnit.MINUTES);
            }
            return name;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }

    }



}
