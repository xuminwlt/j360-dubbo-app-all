package me.j360.dubbo.manager.helper;

import com.pajk.ic.api.model.domain.item.*;
import com.pajk.ic.api.model.enums.GoodsStatus;
import com.pajk.ic.api.model.enums.ItemStatus;
import com.pajk.ic.api.model.enums.Source;
import com.pajk.ic.api.model.param.item.ItemAddDTO;
import com.pajk.ic.api.model.param.item.ItemUpdDTO;
import com.pajk.ic.api.model.result.ErrorCode;
import com.pajk.ic.api.model.topic.ItemTO;
import com.pajk.ic.exception.ServiceException;
import com.pajk.ic.manager.CategoryManager;
import com.pajk.ic.proxy.VendorServiceProxy;
import com.pajk.ic.util.JsonUtils;
import com.pajk.ic.util.LangUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public abstract class ItemPubHelper {
	@Autowired
	protected CategoryManager categoryManager;
	
	@Autowired
	protected VendorServiceProxy vendorServiceProxy;
	
	/**
	 * 新建，验证
	 */
	public void validateForInsert(ItemDO item,GoodsDO goods,List<ItemServeConfigDO> configs)throws ServiceException{
		if(item == null || goods == null){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"item or goods is null!");
		}
		if(LangUtils.getLong(item.getCategoryId()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"categoryId is requried!");
		}
		CategoryDO categoryDO = categoryManager.getCategoryById(item.getCategoryId());
		if(categoryDO == null){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"categoryId is not exits!");
		}
		if(!categoryDO.getLeaf()){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"categoryId is must leaf!");
		}
		if(LangUtils.getLong(item.getSellerId()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"sellerId is requried!");
		}
		//调用user-service获取商户信息，进行验证商户状态和权限；
		if(!vendorServiceProxy.checkSeller(item.getSellerId())){
			String errorMsg = String.format("sellerId[%d] is not right!", item.getSellerId());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
		if(StringUtils.isEmpty(item.getTitle())){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"title is requried!");
		}
		if(StringUtils.isEmpty(item.getSubTitle())){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"subTitle is requried!");
		}
		if(StringUtils.isEmpty(item.getAliases())){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"aliases is requried!");
		}
		if(StringUtils.isEmpty(item.getOneWord())){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"oneWord is requried!");
		}
		if(LangUtils.getInteger(item.getStockNum()) <= 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"stockNum is requried!");
		}
		if(StringUtils.isEmpty(item.getDetailUrl())){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"detailUrl is requried!");
		}
		if(StringUtils.isEmpty(item.getPicUrls())){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"picUrls is requried!");
		}
		ItemPicUrls picUrls = ItemPicUrls.newInstance(item.getPicUrls());
		if(LangUtils.getLong(item.getPrice()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"price is requried!");
		}
		if(LangUtils.getLong(item.getCredit()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"credit is requried!");
		}
		ItemFeature feature = item.getItemFeature();
		if(feature == null){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"feature is requried!");
		}
		Long priceMarket=feature.getPriceMarket();
		Long priceVip=feature.getPriceVip();
		Long creditMarket=feature.getCreditMarket();
		Long creditVip=feature.getCreditVip();
		if(LangUtils.getLong(priceMarket) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"priceMarket is requried!"); 
		}
		if(LangUtils.getLong(priceVip) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"priceVip is requried!");
		}
		if(LangUtils.getLong(creditMarket) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"creditMarket is requried!");
		}
		if(LangUtils.getLong(creditVip) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"creditVip is requried!");
		}
		//验证后端商品
		validateForInsert(LangUtils.getLong(item.getId()),goods);
		//验证服务配置
		validateForInsertExtend(item,goods,configs);
	}
	
	/**
	 * 编辑，验证
	 */
	public void validateForUpdate(ItemUpdDTO req,ItemTO dest)throws ServiceException{
		if(dest == null){
			String errorMsg = String.format("can't find ItemAgretion by itemId:%d", req.getItem().getId());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
		ItemDO item = dest.getItem();
		if(item == null){
			String errorMsg = String.format("can't find Item by itemId:%d", req.getItem().getId());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
		if(LangUtils.getLong(req.getItem().getSellerId()) != LangUtils.getLong(item.getSellerId())){
			String errorMsg = String.format("sellerId[%d] is not owner of item[%d],real owner is sellerId[%d]", req.getItem().getSellerId(),req.getItem().getId(),item.getSellerId());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
		if(LangUtils.getLong(req.getItem().getCategoryId()) != LangUtils.getLong(item.getCategoryId())){//商品类目不容许变更
			String errorMsg = String.format("商品类目不容变更,itemId[%d],categoryId[%d],actual_categoryId[%d]", req.getItem().getId(),req.getItem().getCategoryId(),item.getCategoryId());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
		if(item.isPublish()){
			String errorMsg = String.format("can't update Item, because status[%d] is published", dest.getItem().getStatus());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
		//验证后端商品
		validateForUpdate(req.getGoods(),dest.getGoods());
		//验证服务配置
		validateForUpdateExtend(req.getItem(),req.getItemServAdds(),req.getItemServUpds(),req.getItemServDels(),dest);
	}
	
	/**
	 * 新建，属性设置
	 */
	public void propertyForInsert(long itemId,ItemAddDTO itemAgretion){
		itemAgretion.getItem().setSource(Source.sytem.getValue());
		itemAgretion.getItem().setId(itemId);
		itemAgretion.getItem().setStatus(ItemStatus.create.getValue());
		itemAgretion.getItem().setGmtCreate(new Date());
		itemAgretion.getItem().setGmtUpdate(new Date());
		itemAgretion.getItem().setFeature(JsonUtils.getJsonString(itemAgretion.getItem().getItemFeature().getFeature()));
		itemAgretion.getGoods().setItemId(itemId);
		itemAgretion.getGoods().setStatus(GoodsStatus.create.getValue());
		itemAgretion.getGoods().setGmtCreate(new Date());
		itemAgretion.getGoods().setGmtUpdate(new Date());
		propertyForInsertExtend(itemId,itemAgretion.getItemServiceConfig());
	}
	
	/**
	 * 编辑，属性设置
	 */
	public void propertyForUpdate(ItemUpdDTO req,ItemTO dest){
		req.getItem().setSource(Source.sytem.getValue());
		req.getItem().setStatus(dest.getItem().getStatus());
		req.getItem().setGmtCreate(dest.getItem().getGmtCreate());
		req.getItem().setGmtUpdate(new Date());
		req.getItem().setVersion(dest.getItem().getVersion());
		req.getItem().setFeature(JsonUtils.getJsonString(req.getItem().getItemFeature().getFeature()));
		req.getGoods().setStatus(dest.getGoods().getStatus());
		req.getGoods().setGmtCreate(dest.getGoods().getGmtCreate());
		req.getGoods().setGmtUpdate(new Date());
		req.getGoods().setVersion(dest.getGoods().getVersion());
		propertyForUpdateExtend(req.getItem().getId(),req.getItemServAdds(),req.getItemServUpds(),dest.getItemServiceConfig());
	}
	
	/**
	 * 发布商品，验证后端商品
	 */
	private void validateForInsert(long itemId,GoodsDO goods)throws ServiceException{
		if(LangUtils.getLong(goods.getCostCredit()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"goods.costCredit is requried!");
		}
		if(LangUtils.getLong(goods.getCostPrice()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"goods.costPrice is requried!");
		}
		if(LangUtils.getLong(goods.getPayCredit()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"goods.payCredit is requried!");
		}
		if(LangUtils.getLong(goods.getCostCredit()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"goods.costCredit is requried!");
		}
		if(LangUtils.getInteger(goods.getStockNum()) == 0){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"goods.stockNum is requried!");
		}
	}
	
	private void validateForUpdate(GoodsDO src,GoodsDO dest)throws ServiceException{
		if(dest == null){
			String errorMsg = String.format("can't find Goods by itemId:%d", src.getItemId());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
		if(src.getId() == null){
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"goodsId is required!");
		}
		if(LangUtils.getLong(src.getId()) != LangUtils.getLong(dest.getId())){
			String errorMsg = String.format("goodsId[%d] is not right!", src.getId().longValue());
			throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
		}
	}
	
	abstract void validateForInsertExtend(ItemDO item,GoodsDO goods,List<ItemServeConfigDO> configs)throws ServiceException;
	
	abstract void validateForUpdateExtend(ItemDO item,List<ItemServeConfigDO> itemServAdds,List<ItemServeConfigDO> itemServUpds, List<Long> itemServDels,ItemTO dest)throws ServiceException;
	
	abstract void propertyForInsertExtend(long itemId,List<ItemServeConfigDO> itemConfigs);

	abstract void propertyForUpdateExtend(long itemId,List<ItemServeConfigDO> itemConfigAdds,List<ItemServeConfigDO> itemConfigUpds,List<ItemServeConfigDO> itemConfigsDest);
}
