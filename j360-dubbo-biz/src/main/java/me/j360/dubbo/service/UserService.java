package me.j360.dubbo.service;

import com.sun.javafx.tools.packager.Log;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.model.result.user.UserAddResult;
import me.j360.dubbo.api.model.result.user.UserInfoResult;
import me.j360.dubbo.api.model.result.user.UserListResult;
import me.j360.dubbo.api.service.UserService;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.base.exception.ServiceException;
import me.j360.dubbo.exception.ArgumentException;

/**
 * Package: me.j360.dubbo.service
 * User: min_xu
 * Date: 16/8/23 下午2:53
 * 说明：service需要cache掉系统中所有的自定义的异常+其他异常，并返回一个result
 */
public class UserServiceImpl implements UserService {

    public UserInfoResult getUserInfo(UserDTO options) {
        try{
            UserInfoResult result = new UserInfoResult();
            if(options == null){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(),"options is null");
            }
            if(itemId == 0){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(),"itemId is 0!");
            }
            //需要调用
            ItemDO item = itemQryManager.getItemDO(itemId);
            if(item == null){
                result.setItem(item);
                return result;
            }
            if(options.isNeedCategory()){//判断是否需要填充category
                CategoryDO category = categoryManager.getCategoryById(item.getCategoryId());
                result.setCategory(category);
            }
            if(options.isCreditFade()){//判断是否需要填充积分标志
                ItemFeature feature = item.getItemFeature();
                if(options.getUserId() == 0){//未登录
                    if(feature.getCreditMarket() != 0){
                        //设置标志vip
                        feature.putKV(ItemFeatureKey.FLAG_PRICE.getValue(), ""+1);
                    }
                }else{//登录用户
                    UserMock userMock = userServiceProxy.getUser(options.getUserId());
                    if(userMock.isVip()){//设置标志vip
                        //设置标志vip
                        feature.putKV(ItemFeatureKey.FLAG_PRICE.getValue(), ""+1);
                    }
                }
                item.setFeature(JsonUtils.getJsonString(feature.getFeature()));
            }
            result.setItem(item);
            return result;
        }catch(ArgumentException ae){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, ae);
            return new UserInfoResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, re);
            return new UserInfoResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, th);
            return new UserInfoResult(false,ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public UserListResult listUser(UserDTO options) {
        try{
            UserListResult result = new UserListResult();
            if(options == null){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(),"options is null");
            }
            if(itemId == 0){
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(),"itemId is 0!");
            }
            ItemTO item = itemQryManager.getItemTO(itemId, options.isNeedGoods(), options.isNeedConfig());
            result.setItem(item.getItem());
            result.setGoods(item.getGoods());
            result.setItemServiceConfig(item.getItemServiceConfig());
            return result;
        }catch(ArgumentException ae){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, ae);
            return new UserListResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, re);
            return new UserListResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, th);
            return new UserListResult(false,ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    public UserAddResult saveUser() {
        try{
            Log.info("addItem-begin:"+itemAddDTO);
            itemPubManager.insert(itemAddDTO);
            Log.info("addItem-succ:"+itemAddDTO);
            return new UserAddResult();
        }catch(ArgumentException ae){
            Log.error("addItem failure:"+itemAddDTO, ae);
            return new UserAddResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(ServiceException ve){
            Log.error("addItem failure:"+itemAddDTO, ve);
            return new UserAddResult(false,ve.getExceptionCode(),ve.getMessage());
        }catch(RepositoryException re){
            Log.error("addItem failure:"+itemAddDTO, re);
            return new UserAddResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            Log.error("addItem failure:"+itemAddDTO, th);
            return new UserAddResult(false, ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
