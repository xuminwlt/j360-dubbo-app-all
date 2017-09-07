package me.j360.dubbo.batch.manager;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.base.constant.DefaultErrorCode;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.batch.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Package: cn.paomiantv.batch.manager
 * User: min_xu
 * Date: 2017/8/8 下午1:55
 * 说明：
 */

@Slf4j
@Service
public class UserManager {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static String batchUserInfoSql = "INSERT INTO  user_info (uid, like_count, belike_count, post_count, create_time, update_time)\n" +
            "VALUES (?,?,?,?,?,?)\n" +
            "ON DUPLICATE KEY UPDATE like_count = VALUES(like_count),belike_count = VALUES(belike_count),post_count=values(post_count),update_time=VALUES(update_time);";





    public void setUserInfoToDB(List<? extends UserInfo> items) {
        Long time = System.currentTimeMillis();
        try {
            execute(batchUserInfoSql, items, new BatchUpdateCallBack() {
                @Override
                public void execute(PreparedStatement ps, int i) {
                    try {
                        ps.setObject(1, items.get(i).getUid());
                        ps.setObject(2, items.get(i).getLikeCount());
                        ps.setObject(3, items.get(i).getBelikeCount());
                        ps.setObject(4, items.get(i).getPostCount());
                        ps.setObject(5, time);
                        ps.setObject(6, time);
                    } catch (Exception e) {
                        log.error("批量插入user_info数据时异常: {}", items.get(i),e);
                    }
                }
            });
        } catch (Exception e) {
            throw new RepositoryException(DefaultErrorCode.DB_ERROR.getErrorCode(), DefaultErrorCode.DB_ERROR.getErrorMsg(), e);
        }
    }

    public <T> void execute(String sql, final List<T> list, final BatchUpdateCallBack callBack) {
        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                callBack.execute(ps, i);
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

}
