package me.j360.dubbo.batch.writer;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.base.exception.ServiceException;
import me.j360.dubbo.batch.domain.UserInfo;
import me.j360.dubbo.batch.manager.UserManager;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
public class UserInfoJdbcItemWriter implements ItemWriter<UserInfo> {

    @Autowired
    private UserManager userManager;

    @Override
    public void write(List<? extends UserInfo> items) throws Exception {
        try {
            userManager.setUserInfoToDB(items);
        } catch (ServiceException | RepositoryException e) {
            log.error("执行UserInfoJdbcItemWriter:write异常: {}", items, e);
            throw e;
        }
    }
}
