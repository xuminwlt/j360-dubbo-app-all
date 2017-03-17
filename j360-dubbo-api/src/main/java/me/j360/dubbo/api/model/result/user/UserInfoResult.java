package me.j360.dubbo.api.model.result.user;


import lombok.Data;
import lombok.NoArgsConstructor;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.base.model.result.ResultSupport;

import java.util.Map;

/**
 * Package: me.j360.dubbo.api.model.result
 * User: min_xu
 * Date: 16/8/23 下午3:46
 * 说明：result定位于传输rpc的返回类,对DTO进行二次整合
 */
@Data
@NoArgsConstructor
public class UserInfoResult extends ResultSupport {

    private Map<Long, ErrorCode> errorMap;

    public UserInfoResult(boolean success,int resultCode,String resultMsg){
        super(success,resultCode,resultMsg);
    }

}
