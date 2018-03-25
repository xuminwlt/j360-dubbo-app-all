package me.j360.dubbo.cache.dao;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.cache.domain.CacheModelEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;

/**
 * @author: min_xu
 * @date: 2017/11/30 下午4:58
 * 说明：
 */

@Slf4j
@Repository
public class CacheDao {


    @Autowired
    private JdbcTemplate masterJdbcTemplate;

    private static String UPDATE_SQL_TEMPLATE = "INSERT INTO  __TABLE__ (__IDCOLUMN__, __COLUMN__, create_time, update_time)\n" +
            "VALUES (?,?,?,?)\n" +
            "ON DUPLICATE KEY UPDATE __COLUMN__ = VALUES(__COLUMN__), update_time = VALUES(update_time);";


    private static String FORCE_UPDATE_SQL_TEMPLATE = "UPDATE  __TABLE__  SET __COLUMN__ = ?, update_time = ? \n" +
            "WHERE __IDCOLUMN__ = ?";


    private static String SELECT_SQL_TEMPLATE = "";


    /**
     * 获取Long型统计数据
     * @param cacheModelEnum
     * @param id
     * @return
     */
    public Long getCount(CacheModelEnum cacheModelEnum, Long id) {
        String sql = StringUtils.replace(SELECT_SQL_TEMPLATE, "__TABLE__", cacheModelEnum.getTable());
        sql = StringUtils.replace(sql, "__IDCOLUMN__", cacheModelEnum.getIdColumn());
        sql = StringUtils.replace(sql, "__COLUMN__", cacheModelEnum.getColumn());

        Map<String, Object> map = masterJdbcTemplate.queryForMap(sql);
        return Objects.isNull(map.getOrDefault(cacheModelEnum.getColumn(), null))?0L:Long.valueOf(map.get(cacheModelEnum.getColumn()).toString());
    }


    /**
     * 更新并替换Long数据
     * mapper不支持 on 语句,使用jdbc连接主库完成
     * @param cacheModelEnum
     * @param id
     * @param value
     */
    public void updateCountValue(CacheModelEnum cacheModelEnum, Long id, Long value) {
        //log.info("mapper is null:{}",Objects.isNull(mapper));
        /*mapper.updateCountValue(cacheModelEnum.getTable(),
                cacheModelEnum.getColumn(),
                cacheModelEnum.getIdColumn(),
                id, value);*/

        if (cacheModelEnum.isForceUpdate()) {
            //强制更新表
            String sql = StringUtils.replace(FORCE_UPDATE_SQL_TEMPLATE, "__TABLE__", cacheModelEnum.getTable());
            sql = StringUtils.replace(sql, "__IDCOLUMN__", cacheModelEnum.getIdColumn());
            sql = StringUtils.replace(sql, "__COLUMN__", cacheModelEnum.getColumn());
            Long time = System.currentTimeMillis();
            masterJdbcTemplate.update(sql, value, time, id);
        } else {
            String sql = StringUtils.replace(UPDATE_SQL_TEMPLATE, "__TABLE__", cacheModelEnum.getTable());
            sql = StringUtils.replace(sql, "__IDCOLUMN__", cacheModelEnum.getIdColumn());
            sql = StringUtils.replace(sql, "__COLUMN__", cacheModelEnum.getColumn());
            Long time = System.currentTimeMillis();
            masterJdbcTemplate.update(sql, id, value, time, time);
        }

    }
}
