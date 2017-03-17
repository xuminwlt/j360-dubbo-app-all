package me.j360.dubbo.dao.mapper;

/**
 * Package: me.j360.dubbo.dao.mapper
 * User: min_xu
 * Date: 16/8/23 下午2:59
 * 说明：
 */
public interface UserMapper {

    /**
     * 添加商品服务配置
     * @param
     */
    void add(UserDo userDo);

    int count();
}
