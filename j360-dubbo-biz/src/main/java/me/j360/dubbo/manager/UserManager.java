package me.j360.dubbo.manager;

import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.model.result.user.UserInfoResult;
import me.j360.dubbo.base.exception.ServiceException;
import me.j360.dubbo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Package: me.j360.dubbo.manager
 * User: min_xu
 * Date: 16/8/23 下午2:53
 * 说明：
 */

@Slf4j
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

        /*TransactionCallback<Map<Long, ErrorCode>> transactionCallback = new TransactionCallback<Map<Long, ErrorCode>>() {
            @Override
            public Map<Long, ErrorCode> doInTransaction(TransactionStatus status) {
                Map<Long, ErrorCode> errorMap = new HashMap<Long, ErrorCode>();
                try {
                    //绑定用户
                    userVoucherPassRepository.updateUserAndVerison(uid, userVoucherPassDO.getVersion(), voucherNo, UserVoucherPassStatus.binding.getValue());
                    //生成券密和用户绑定的流水
                    voucherPassTransRepository.create(createVoucherPassTrans(uid, voucherBatchDO, userVoucherPassDO));
                    //生成券密和商品的使用次数关系
                    List<VoucherPassItemDO> voucherPassItems = voucherPassItemRepository.queryAllByVoucherPassId(userVoucherPassDO.getId());
                    if (CollectionUtils.isEmpty(voucherPassItems)) {
                        //没有被使用过，使用过不需要再insert券密和商品的使用次数
                        List<VoucherItemDO> voucherItemDOs = voucherItemRepository.queryAllByVoucherId(userVoucherPassDO.getVoucherId());
                        if (!CollectionUtils.isEmpty(voucherItemDOs)) {
                            for (VoucherItemDO voucherItemDO : voucherItemDOs) {
                                VoucherPassItemDO voucherPassItemDO = createVoucherPassItem(voucherItemDO, userVoucherPassDO);
                                voucherPassItems.add(voucherPassItemDO);
                            }
                        }
                        voucherPassItemRepository.insertBatch(voucherPassItems);
                    }
                } catch (Exception e) {
                    logger.error("user-bind-voucherpass error", e);
                    status.setRollbackOnly();
                    errorMap.put(uid, ErrorCode.DB_ERROR);
                }
                return null;
            }
        };

        try {
            errorMap = transactionTemplate.execute(transactionCallback);
        } catch (Exception e) {
            logger.error("user-bind-voucherpass transsction error !", e);

        }*/
        return userInfoResult;
    }
}
