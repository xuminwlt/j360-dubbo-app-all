package me.j360.dubbo.repository;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.api.model.domain.UserDO;
import me.j360.dubbo.base.constant.AppConfig;
import me.j360.dubbo.base.constant.UserKeys;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.base.model.result.PageDO;
import me.j360.dubbo.dao.mapper.UserMapper;
import me.j360.dubbo.exception.ArgumentException;
import me.j360.dubbo.modules.util.collection.ListUtil;
import me.j360.dubbo.util.ModelUtil;
import me.j360.dubbo.util.Params;
import org.apache.commons.collections4.ListUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Package: me.j360.dubbo.dao.repository
 * User: min_xu
 * Date: 16/8/23 下午2:57
 * 说明：
 */

@Repository
public class UserRepository {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedissonClient redissonClient;

    public PageDO<List<UserDO>> pageList(int pageNo, int pageSize){
        PageDO<List<UserDO>> page = new PageDO<List<UserDO>>();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        try{
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("pageNo", (pageNo-1)*pageSize);
            param.put("pageSize", pageSize);

           /* List<UserDO> datas = userMapper.pageList(param);
            int recordCount = userMapper.pageCount(param);
            int recordSize  = datas == null?0:datas.size();
            page.setRecordCount(recordCount);
            page.setRecordSize(recordSize);
            page.setData(datas);*/
            return page;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }
    }


    public UserDO getGoods(long itemId) {
        try{
            Params params = new Params("itemId",itemId);
            /*List<UserDO> goods = userMapper.getByIdDyn(params.getResult());
            if(CollectionUtils.isEmpty(goods)){
                throw new ArgumentException(ErrorCode.GOODS_FIND_ERROR.getErrorCode(),ErrorCode.GOODS_FIND_ERROR.getErrorMsg());
            }
            if(goods.size() > 1){
                throw new ArgumentException(ErrorCode.GOODS_FIND_SIZE_ERROR.getErrorCode(),ErrorCode.GOODS_FIND_SIZE_ERROR.getErrorMsg());
            }
            UserDO data = goods.get(0);
            return data;*/
            return null;
        }catch(ArgumentException ex){
            throw ex;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }
    }

    public void create(UserDO userDO) {
        userMapper.add(userDO);
    }

    public int count() {
        return userMapper.count();
    }

    public List<UserDO> list() {
        return userMapper.list();
    }

    public List<UserDO> list(List<Long> ids) {
        return userMapper.list();
    }
    /**
     * redission 作为二级缓存DAO层的案例
     * @param itemId
     * @return
     */
    public UserDO getGoodsCacheable(long itemId) {
        try{
            RBucket<UserDO> rBucket = redissonClient.getBucket(String.format(UserKeys.USER_DO_ID, itemId, JsonJacksonCodec.INSTANCE));
            if (rBucket.isExists()) {
                rBucket.expire(AppConfig.COMMON_CACHE_DAYS, TimeUnit.MINUTES);
                return rBucket.get();
            }
            UserDO topicDO = getGoods(itemId);
            if (Objects.nonNull(topicDO)) {
                redissonClient.getBucket(String.format(UserKeys.USER_DO_ID, itemId),JsonJacksonCodec.INSTANCE)
                        .setAsync(topicDO, AppConfig.COMMON_CACHE_DAYS, TimeUnit.MINUTES);
            }
            return topicDO;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }

    }


    public List<UserDO> findIn(List<Long> ids) {
        try{
            ids = ids.stream().distinct().collect(Collectors.toList());
            ListUtil.sort(ids);
            RBuckets rBuckets = redissonClient.getBuckets(JsonJacksonCodec.INSTANCE);
            Map<String, UserDO> vpMaps = rBuckets.get(ModelUtil.formatStrings(UserKeys.USER_DO_ID, ids));

            List<UserDO> list = Lists.newArrayListWithCapacity(ids.size());
            List<Long> redisIds = Lists.newArrayListWithCapacity(ids.size());
            ids.stream().forEach( id -> {
                String key = String.format(UserKeys.USER_DO_ID, id);
                if (vpMaps.containsKey(key)) {
                    UserDO topicDO = vpMaps.get(key);
                    if (Objects.nonNull(topicDO)) {
                        list.add(topicDO);
                        redisIds.add(id);

                        //延迟过期
                        redissonClient.getBucket(key,JsonJacksonCodec.INSTANCE)
                                .expire(AppConfig.COMMON_CACHE_DAYS, TimeUnit.MINUTES);
                    }
                }
            });

            List<Long> lastIds = ListUtils.removeAll(ids, redisIds);
            if (CollectionUtils.isNotEmpty(lastIds)) {
                List<UserDO> dbList =  list(lastIds);
                if (CollectionUtils.isNotEmpty(dbList)) {
                    list.addAll(dbList);

                    dbList.stream().forEach(topicDO -> {
                        redissonClient.getBucket(String.format(UserKeys.USER_DO_ID, topicDO.getUid()),JsonJacksonCodec.INSTANCE)
                                .setAsync(topicDO, AppConfig.COMMON_CACHE_DAYS, TimeUnit.MINUTES);
                    });
                }
            }

            return list;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }
    }



}
