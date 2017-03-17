package me.j360.dubbo.api.model.result.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.j360.dubbo.base.model.result.ResultSupport;

/**
 * Package: me.j360.dubbo.api.model.result.user
 * User: min_xu
 * Date: 2017/3/16 下午4:07
 * 说明：
 */
@Data
@NoArgsConstructor
public class UserAddResult  extends ResultSupport {


    public UserAddResult(boolean success,int resultCode,String resultMsg){
        super(success,resultCode,resultMsg);
    }

}
