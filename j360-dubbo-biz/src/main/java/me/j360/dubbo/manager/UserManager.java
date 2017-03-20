package me.j360.dubbo.manager;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.model.result.user.UserInfoResult;
import me.j360.dubbo.base.exception.ServiceException;
import me.j360.dubbo.dao.model.UserDO;
import me.j360.dubbo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package: me.j360.dubbo.manager
 * User: min_xu
 * Date: 16/8/23 下午2:53
 * 说明：
 */

@Slf4j
@Component
public class UserManager {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionTemplate transactionTemplate;

    //依赖外部的RPC写在Manager中,比如
    //@Autowired
    //private OtherRpcService otherRpcService;

    /**
     * 发布商品
     */
    public long insert(UserDTO req){
        long itemId = 0L;
        String borrowedId = null;
        boolean success = false;

        //验证
        if(req == null){
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"itemAgretion is null!");
        }

        //属性验证

        //入库操作
        //userRepository.insert();
        success = true;
        return itemId;

    }


    public UserInfoResult bind(UserDTO userDTO){
        UserInfoResult userInfoResult = new UserInfoResult();

        Map<Long, ErrorCode> errorCodeMap = Maps.newHashMap();
        TransactionCallback<Map<Long, ErrorCode>> transactionCallback = new TransactionCallback<Map<Long, ErrorCode>>() {
            @Override
            public Map<Long, ErrorCode> doInTransaction(TransactionStatus status) {
                Map<Long, ErrorCode> errorMap = new HashMap<Long, ErrorCode>();
                try {
                    //slave
                    userRepository.list();

                    //master
                    UserDO userDO = new UserDO();
                    userDO.setName("1");
                    userRepository.create(userDO);

                    userDO.setName("2");
                    userRepository.create(userDO);

                    //master
                    userRepository.list();
                } catch (Exception e) {
                    log.error("user-bind-voucherpass error", e);
                    status.setRollbackOnly();
                    errorMap.put(1L,ErrorCode.DB_ERROR);
                }
                return errorMap;
            }
        };

        try {
            errorCodeMap = transactionTemplate.execute(transactionCallback);
        } catch (Exception e) {
            log.error("user-bind-voucherpass transsction error !", e);

        }
        userInfoResult.setErrorMap(errorCodeMap);
        return userInfoResult;
    }


    public int count() {
        return userRepository.count();
    }

    public List<UserDO> list() {
        List<UserDO> list = userRepository.list();
        log.info(list.toString());
        return list;
    }
}
