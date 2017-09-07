package me.j360.dubbo.batch.mapper;

import me.j360.dubbo.batch.domain.UserID;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;



public class UserRowMapper implements RowMapper<UserID> {

    @Override
    public UserID mapRow(ResultSet resultSet, int i) throws SQLException {
        return UserID.builder()
                .id(resultSet.getLong("id"))
                .build();
    }
}
