package me.j360.dubbo.manager;

import com.pajk.ic.api.model.domain.item.VoucherDO;
import com.pajk.ic.api.model.enums.VoucherStatus;
import com.pajk.ic.api.model.param.item.VoucherQueryDTO;
import com.pajk.ic.api.model.result.ErrorCode;
import com.pajk.ic.api.model.result.item.VoucherListQueryResult;
import com.pajk.ic.api.model.result.item.VoucherQueryResult;
import com.pajk.ic.api.model.result.item.VoucherResult;
import com.pajk.ic.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoucherManager {
	
	@Autowired
	private VoucherRepository voucherRepository;

	public VoucherResult create(VoucherDO voucherDO){
		VoucherResult result = new VoucherResult();
		voucherDO.setGmtCreated(new Date());
		voucherDO.setGmtModified(new Date());
		voucherDO.setStatus(VoucherStatus.CREATE.getStatus());
		try {
			voucherRepository.insert(voucherDO);
			result.setVoucherDO(voucherDO);
		} catch (Exception e) {
			result.setErrorCode(ErrorCode.DB_ERROR);
		}
		return result;
	}
	
	public VoucherResult update(VoucherDO voucherDO){
		VoucherResult result = new VoucherResult();
		voucherDO.setGmtModified(new Date());
		try {
			VoucherDO voucherDO2 = voucherRepository.selectById(voucherDO.getId());
			if(VoucherStatus.ENABLE.isEquals(voucherDO2.getStatus())){
				result.setErrorCode(ErrorCode.VOUCHER_ENABLE_NOT_UPDATE);
			}
			voucherRepository.updateSelective(voucherDO);
			result.setVoucherDO(voucherDO);
		} catch (Exception e) {
			result.setErrorCode(ErrorCode.DB_ERROR);
		}
		return result;
	}
	
	public VoucherQueryResult selectById(Long id){
		VoucherQueryResult result = new VoucherQueryResult();
		try {
			VoucherDO voucherDO = voucherRepository.selectById(id);
			result.setVoucherDO(voucherDO);
		} catch (Exception e) {
			result.setErrorCode(ErrorCode.DB_ERROR);
		}
		return result;
	}
	
	public VoucherResult enable(Long id){
		VoucherResult result = new VoucherResult();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("status", VoucherStatus.ENABLE.getStatus());
		try {
			voucherRepository.updateStatusById(params);
		} catch (Exception e) {
			result.setErrorCode(ErrorCode.DB_ERROR);
		}
		return result;
	}
	
	public VoucherResult disable(Long id){
		VoucherResult result = new VoucherResult();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("status", VoucherStatus.DISABLE.getStatus());
		try {
			voucherRepository.updateStatusById(params);
		} catch (Exception e) {
			result.setErrorCode(ErrorCode.DB_ERROR);
		}
		return result;
	}
	
	public VoucherListQueryResult queryByParams(VoucherQueryDTO params){
		VoucherListQueryResult result = new VoucherListQueryResult();
		try {
			List<VoucherDO> list = voucherRepository.queryByParams(params);
			int count = voucherRepository.countListByParams(params);
			result.setVoucherDOs(list);
			result.setTotalCount(count);
		} catch (Exception e) {
			result.setErrorCode(ErrorCode.DB_ERROR);
		}
		return result;
	}
}
