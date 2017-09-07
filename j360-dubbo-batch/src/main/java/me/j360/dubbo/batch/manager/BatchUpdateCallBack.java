package me.j360.dubbo.batch.manager;

import java.sql.PreparedStatement;


public interface BatchUpdateCallBack {

    void execute(PreparedStatement ps, int i);
}
