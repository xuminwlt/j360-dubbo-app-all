package me.j360.dubbo.batch.process;

import me.j360.dubbo.batch.domain.UserID;
import me.j360.dubbo.batch.domain.UserInfo;
import me.j360.dubbo.batch.manager.UserManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;


public class UserInfoItemProcess implements ItemProcessor<UserID, UserInfo> {

    @Autowired
    private UserManager userManager;

    @Override
    public UserInfo process(UserID item) throws Exception {

        UserInfo userInfo = new UserInfo();
        userInfo.setUid(item.getId());

        return userInfo;
    }
}
