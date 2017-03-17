package me.j360.dubbo.service;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.model.result.user.UserAddResult;
import me.j360.dubbo.api.model.result.user.UserInfoResult;
import me.j360.dubbo.api.model.result.user.UserListResult;
import me.j360.dubbo.api.service.UserService;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.base.exception.ServiceException;
import me.j360.dubbo.exception.ArgumentException;
import me.j360.dubbo.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Package: me.j360.dubbo.service
 * User: min_xu
 * Date: 16/8/23 下午2:53
 * 说明：service需要cache掉系统中所有的自定义的异常+其他异常，并返回一个result
 */

@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;


    public UserInfoResult getUserInfo(UserDTO options) {
        try{
            UserInfoResult result = new UserInfoResult();
            if(options == null){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(),"options is null");
            }

            return result;
        }catch(ArgumentException ae){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, options);
            log.error(errorMsg, ae);
            return new UserInfoResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, options);
            log.error(errorMsg, re);
            return new UserInfoResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, options);
            log.error(errorMsg, th);
            return new UserInfoResult(false,ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public UserListResult listUser(UserDTO options) {
        try{
            UserListResult result = new UserListResult();
            if(options == null){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(),"options is null");
            }

            return result;
        }catch(ArgumentException ae){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, options);
            log.error(errorMsg, ae);
            return new UserListResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, options);
            log.error(errorMsg, re);
            return new UserListResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, options);
            log.error(errorMsg, th);
            return new UserListResult(false,ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public UserAddResult saveUser(UserDTO userDTO) {
        try{
            log.info("addItem-begin:"+userDTO);
            userManager.insert(userDTO);
            log.info("addItem-succ:"+userDTO);
            return new UserAddResult();
        }catch(ArgumentException ae){
            log.error("addItem failure:"+userDTO, ae);
            return new UserAddResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(ServiceException ve){
            log.error("addItem failure:"+userDTO, ve);
            return new UserAddResult(false,ve.getExceptionCode(),ve.getMessage());
        }catch(RepositoryException re){
            log.error("addItem failure:"+userDTO, re);
            return new UserAddResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            log.error("addItem failure:"+userDTO, th);
            return new UserAddResult(false, ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


}
