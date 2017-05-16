package me.j360.dubbo.service;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.api.model.domain.UserDO;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.service.UserService;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.base.exception.ServiceException;
import me.j360.dubbo.base.model.result.DefaultPageResult;
import me.j360.dubbo.base.model.result.DefaultResult;
import me.j360.dubbo.exception.ArgumentException;
import me.j360.dubbo.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Package: me.j360.dubbo.service
 * User: min_xu
 * Date: 16/8/23 下午2:53
 * 说明：service需要cache掉系统中所有的自定义的异常+其他异常，并返回一个result
 */

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;


    public DefaultResult<UserDO> getUserInfo(UserDTO options) {
        try{
            log.info("开始执行:options:{}",options);
            if(options == null){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(),"options is null");
            }

            UserDO userDO = new UserDO();
            log.info("执行结束:result:{}",userDO.toString());
            return DefaultResult.success(userDO);
        }catch(ArgumentException ae){
            String errorMsg = String.format("getItem failure:itemId:%s:", options);
            log.error(errorMsg, ae);
            return DefaultResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItem failure:itemId:%s:", options);
            log.error(errorMsg, re);
            return DefaultResult.fail(re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItem failure:itemId:%s:", options);
            log.error(errorMsg, th);
            return DefaultResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public DefaultPageResult<UserDO> listUser(UserDTO options) {
        try{

            UserDO userDO = new UserDO();
            log.info("执行结束:result:{}",userDO.toString());

            return DefaultPageResult.success(Collections.singletonList(userDO));
        }catch(ArgumentException ae){
            String errorMsg = String.format("getItemAgretion failure:itemId:%s:", options);
            log.error(errorMsg, ae);
            return DefaultPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItemAgretion failure:itemId:%s:", options);
            log.error(errorMsg, re);
            return DefaultPageResult.fail(re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItemAgretion failure:itemId:%s:", options);
            log.error(errorMsg, th);
            return DefaultPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public DefaultResult<UserDO> saveUser(UserDTO userDTO) {
        try{
            log.info("addItem-begin:"+userDTO);
            userManager.insert(userDTO);
            log.info("addItem-succ:"+userDTO);
            return DefaultResult.success();
        }catch(ArgumentException ae){
            log.error("addItem failure:"+userDTO, ae);
            return DefaultResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch(ServiceException ve){
            log.error("addItem failure:"+userDTO, ve);
            return DefaultResult.fail(ve.getExceptionCode(),ve.getMessage());
        }catch(RepositoryException re){
            log.error("addItem failure:"+userDTO, re);
            return DefaultResult.fail(re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            log.error("addItem failure:"+userDTO, th);
            return DefaultResult.fail( ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


}
