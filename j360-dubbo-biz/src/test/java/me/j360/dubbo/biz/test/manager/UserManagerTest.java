package me.j360.dubbo.biz.test.manager;

import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.model.result.user.UserInfoResult;
import me.j360.dubbo.manager.UserManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserManagerTest extends AbstractManagerTest {

    @Autowired
    private UserManager userManager;

    @Test
    public void addUserTest(){
        UserInfoResult tree = userManager.bind(new UserDTO());
        print(tree);

        //userManager.count();

        userManager.list();
    }


}
