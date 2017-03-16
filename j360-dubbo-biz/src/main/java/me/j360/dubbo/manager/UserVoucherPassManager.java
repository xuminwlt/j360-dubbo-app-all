package me.j360.dubbo.manager;

import me.j360.dubbo.api.constant.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class UserVoucherPassManager {

    private static final Logger logger = LoggerFactory.getLogger(UserVoucherPassManager.class);
	@Autowired
	private UserVoucherPassRepository userVoucherPassRepository;
	@Autowired
	private VoucherBatchRepository voucherBatchRepository;
	@Autowired
	private VoucherPassTransRepository voucherPassTransRepository;
	@Autowired
	private VoucherRepository voucherRepository;
	@Autowired
	private VoucherItemRepository voucherItemRepository;
	@Autowired
	private VoucherPassItemRepository voucherPassItemRepository;
	@Autowired
	private ItemQryRepository itemQryRepository;
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	public UserVoucherBindResult bind(UserVoucherBindDTO userVoucherBindDTO){
		UserVoucherBindResult userVoucherBindResult = new UserVoucherBindResult();
		List<UserVoucherPassDTO> list = userVoucherBindDTO.getList();
		if (CollectionUtils.isEmpty(list)) {
			userVoucherBindResult.setErrorCode(ErrorCode.PARAM_ERROR);
            return userVoucherBindResult;
		}
		Map<Long, ErrorCode> errorMap = new HashMap<Long, ErrorCode>();
		for (UserVoucherPassDTO userVoucherPassDTO : list) {
	       if (userVoucherBindDTO == null || userVoucherPassDTO.getUserId() <= 0) {
	    	   errorMap.put(userVoucherPassDTO.getUserId(), ErrorCode.PARAM_ERROR);
	    	   continue;
	        }
	        final long uid = userVoucherPassDTO.getUserId();
			final String voucherNo = userVoucherPassDTO.getVoucherNo();
			final String secret = userVoucherPassDTO.getSecret();
			UserVoucherPassDTO userVoucherPassQueryDTO = new UserVoucherPassDTO();
			userVoucherPassQueryDTO.setSecret(secret);
			userVoucherPassQueryDTO.setVoucherNo(voucherNo);
			List<UserVoucherPassDO> userVoucherPassDOs = userVoucherPassRepository.getAllListByParams(userVoucherPassQueryDTO);
			if (CollectionUtils.isEmpty(userVoucherPassDOs)) {
				 errorMap.put(uid, ErrorCode.VOUCHER_NOT_FOUND);
				 continue;
			}
			final UserVoucherPassDO userVoucherPassDO = userVoucherPassDOs.get(0);
			if (userVoucherPassDO == null 
					|| UserVoucherPassStatus.deleted.isEqual(userVoucherPassDO.getStatus())
					|| UserVoucherPassStatus.exception.isEqual(userVoucherPassDO.getStatus())) {
				errorMap.put(uid, ErrorCode.VOUCHER_NOT_FOUND);
				continue;
			}
			if (UserVoucherPassStatus.binding.isEqual(userVoucherPassDO.getStatus())) {
				errorMap.put(uid, ErrorCode.VOUCHER_BIND);
				continue;
			}
			
			final VoucherBatchDO voucherBatchDO = voucherBatchRepository.selectById(userVoucherPassDO.getBatchId());
	        if (TimeUtils.isFirstAfterSecond(new Date(),voucherBatchDO.getGmtEffectEnd())) {
	        	errorMap.put(uid, ErrorCode.VOUCHER_NOT_AVAILABLE);
				continue;
	        }
	        
			VoucherDO voucherDO = voucherRepository.selectById(voucherBatchDO.getVoucherId());
	        if (VoucherPassType.GENERATION.isEqual(voucherDO.getPassType())) {
				if(StringUtils.isBlank(userVoucherPassDTO.getSecret()) || !userVoucherPassDTO.getSecret().equals(userVoucherPassDO.getSecret())){
					errorMap.put(uid, ErrorCode.VOUCHER_PASS_ERROR);
					continue;
				}
			}
	        TransactionCallback<Map<Long, ErrorCode>> transactionCallback = new TransactionCallback<Map<Long, ErrorCode>>() {
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
                errorMap.put(uid, ErrorCode.DB_ERROR);
            }
        }
        if (errorMap != null) {
            userVoucherBindResult.setErrorMap(errorMap);
        }
        return userVoucherBindResult;
    }
	
	private VoucherPassTransDO createVoucherPassTrans(Long userId, VoucherBatchDO voucherBatchDO,UserVoucherPassDO voucherPassDO){
		VoucherPassTransDO transDO = new VoucherPassTransDO();
		transDO.setGmtCreated(new Date());
		transDO.setGmtModified(new Date());
		transDO.setGmtEffectBegin(voucherBatchDO.getGmtEffectBegin());
		transDO.setGmtEffectEnd(voucherBatchDO.getGmtEffectEnd());
		transDO.setSecret(voucherPassDO.getSecret());
		transDO.setUserId(userId);
		transDO.setVoucherNo(voucherPassDO.getVoucherNo());
		transDO.setVoucherPassId(voucherPassDO.getId());
		transDO.setVoucherTransType(UserVoucherPassStatus.binding.getValue());
		return transDO;
	}
	
	private VoucherPassItemDO createVoucherPassItem(VoucherItemDO voucherItemDO,UserVoucherPassDO userVoucherPassDO){
		VoucherPassItemDO passItemDO = new VoucherPassItemDO();
		passItemDO.setBal(voucherItemDO.getNum());
		passItemDO.setGmtCreated(new Date());
		passItemDO.setGmtModified(new Date());
		passItemDO.setItemId(voucherItemDO.getItemId());
		passItemDO.setItemName(voucherItemDO.getItemName());
		passItemDO.setVersion(0L);
		passItemDO.setVoucherNo(userVoucherPassDO.getVoucherNo());
		passItemDO.setVoucherPassId(userVoucherPassDO.getId());
		passItemDO.setVoucherName(voucherItemDO.getVoucherName());
		return passItemDO;
	}
	
	public UserVoucherItemListQueryResult queryVoucherItemsByUserId(Long userId){
		UserVoucherItemListQueryResult result = new UserVoucherItemListQueryResult();
		List<UserVoucherPassDO> list = new ArrayList<UserVoucherPassDO>();
		UserVoucherPassDTO userVoucherPassDTO = new UserVoucherPassDTO();
		userVoucherPassDTO.setUserId(userId);
		userVoucherPassDTO.setStatus(UserVoucherPassStatus.binding.getValue());
		list = userVoucherPassRepository.getAllListByParams(userVoucherPassDTO);
		Map<VoucherQueryResult, List<ItemDO>> map = new HashMap<VoucherQueryResult, List<ItemDO>>();
		for (UserVoucherPassDO userVoucherPassDO : list) {
			List<VoucherItemDO> voucherItemDOs = voucherItemRepository.queryAllByVoucherId(userVoucherPassDO.getVoucherId());
			Collection<Long> itemIds = new ArrayList<Long>();
			for (VoucherItemDO voucherItemDO : voucherItemDOs) {
				itemIds.add(voucherItemDO.getItemId());
			}
			VoucherQueryResult queryResult = new VoucherQueryResult();
			VoucherDO voucherDO = voucherRepository.selectById(userVoucherPassDO.getVoucherId());
			queryResult.setVoucherDO(voucherDO);
			VoucherBatchDO voucherBatchDO = voucherBatchRepository.selectById(userVoucherPassDO.getBatchId());
			queryResult.setGmtEffectBegin(voucherBatchDO.getGmtEffectBegin());
			queryResult.setGmtEffectEnd(voucherBatchDO.getGmtEffectEnd());
			if (TimeUtils.isFirstAfterSecond(new Date(), voucherBatchDO.getGmtEffectEnd())) {
				queryResult.setVoucherEffectType(VoucherEffectType.EXPIRE.getValue());
			}
			List<ItemDO> items = itemQryRepository.getItems(itemIds);
			map.put(queryResult, items);
		}
		result.setMap(map);
		return result;
	}
}
