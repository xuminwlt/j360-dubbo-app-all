package me.j360.dubbo.service;

/**
 * Package: me.j360.dubbo.service
 * User: min_xu
 * Date: 16/8/23 下午2:53
 * 说明：service需要cache掉系统中所有的自定义的异常+其他异常，并返回一个result
 */
public class UserService {


    public ItemAddResult addItem(ItemAddDTO itemAddDTO) {
        try{
            Log.info("addItem-begin:"+itemAddDTO);
            itemPubManager.insert(itemAddDTO);
            Log.info("addItem-succ:"+itemAddDTO);
            return new ItemAddResult();
        }catch(ArgumentException ae){
            Log.error("addItem failure:"+itemAddDTO, ae);
            return new ItemAddResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(ServiceException ve){
            Log.error("addItem failure:"+itemAddDTO, ve);
            return new ItemAddResult(false,ve.getExceptionCode(),ve.getMessage());
        }catch(RepositoryException re){
            Log.error("addItem failure:"+itemAddDTO, re);
            return new ItemAddResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            Log.error("addItem failure:"+itemAddDTO, th);
            return new ItemAddResult(false,ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    public UserServeBalTransQueryResult queryUserServeBalTrans(UserServeBalTransQuery userServeBalTransQuery) {
        if (userServeBalTransQuery == null) {
            return new UserServeBalTransQueryResult();
        }
        return userServeBalManager.queryUserServeBalTrans(userServeBalTransQuery);
    }

    public ItemAgretionResult getItemAgretion(long itemId,ItemAgretionOptionDTO options) {
        try{
            ItemAgretionResult result = new ItemAgretionResult();
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
            return new ItemAgretionResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, re);
            return new ItemAgretionResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItemAgretion failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, th);
            return new ItemAgretionResult(false,ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


    public ItemResult getItem(long itemId,ItemOptionDTO options) {
        try{
            ItemResult result = new ItemResult();
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
            return new ItemResult(false,ae.getExceptionCode(),ae.getMessage());
        }catch(RepositoryException re){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, re);
            return new ItemResult(false,re.getExceptionCode(),re.getMessage());
        }catch(Throwable th){
            String errorMsg = String.format("getItem failure:itemId:%d:"+options, itemId);
            Log.error(errorMsg, th);
            return new ItemResult(false,ErrorCode.SYS_ERROR.getErrorCode(),ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }


}
