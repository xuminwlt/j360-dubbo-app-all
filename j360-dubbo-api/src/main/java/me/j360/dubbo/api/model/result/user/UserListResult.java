package me.j360.dubbo.api.model.result.user;


import lombok.Data;
import lombok.NoArgsConstructor;
import me.j360.dubbo.base.model.result.PageResultSupport;

/**
 * Package: me.j360.dubbo.api.model.result.user
 * User: min_xu
 * Date: 16/8/23 下午3:48
 * 说明：
 */
@Data
@NoArgsConstructor
public class UserListResult extends PageResultSupport {

    public UserListResult(boolean success,int resultCode,String resultMsg){
        super(success,resultCode,resultMsg);
    }
}
