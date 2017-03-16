package me.j360.dubbo.manager;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: me.j360.dubbo.manager
 * User: min_xu
 * Date: 16/8/23 下午2:53
 * 说明：
 */

@Slf4j
public class UserManager {

    @Autowired
    private ItemPubHelperFactory itemPubHelperFactory;
    @Autowired
    private ItemPubRepository itemPubRepository;
    @Autowired
    private ItemQryManager itemQryManager;
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private IDPool itemIdGenerator;
    @Autowired
    private VendorServiceProxy vendorServiceProxy;

    /**
     * 发布商品
     */
    public long insert(ItemAddDTO req){
        long itemId = 0L;
        String borrowedId = null;
        boolean success = false;
        try{
            borrowedId = itemIdGenerator.borrow();
            itemId = Long.parseLong(borrowedId);
            //验证
            if(req == null){
                throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"itemAgretion is null!");
            }
            ItemDO item = req.getItem();
            if(item == null){
                throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"item is null!");
            }
            if(item.getCategoryId() == null || item.getCategoryId() == 0){
                throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"categoryId is requried!");
            }
            CategoryDO category = categoryManager.getCategoryById(item.getCategoryId());
            ItemPublishType publishType = ItemPublishType.get(category.getCategoryFeature().getPublishType());
            ItemPubHelper pubHelper = itemPubHelperFactory.create(publishType);
            //属性验证
            pubHelper.validateForInsert(item, req.getGoods(), req.getItemServiceConfig());
            //属性设置
            pubHelper.propertyForInsert(itemId, req);
            //入库操作
            itemPubRepository.insert(req.getItem(),req.getGoods(),req.getItemServiceConfig());
            success = true;
            return itemId;
        }finally{
            try {
                if (success) {
                    if (borrowedId != null) {
                        itemIdGenerator.consume(borrowedId);
                    }
                } else {
                    if (borrowedId != null) {
                        itemIdGenerator.giveback(borrowedId);
                    }
                }
            } catch (Throwable t) {
                Log.warn(String.format(
                        "failed to return/consume id from id pool! borrowedId=%s, sucess=%s", borrowedId,
                        success), t);
            }
        }
    }

    /**
     * 更新商品
     */
    public long update(ItemUpdDTO req){
        long itemId = 0L;
        if(req == null){
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"ItemUpdateRequest is null!");
        }
        ItemDO item = req.getItem();
        if(item == null){
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"item is null!");
        }
        if(item.getCategoryId() == null || item.getCategoryId() == 0){
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"categoryId is requried!");
        }
        CategoryDO category = categoryManager.getCategoryById(item.getCategoryId());
        List<ItemServeConfigDO> datas = new ArrayList<ItemServeConfigDO>();
        if(!CollectionUtils.isEmpty(req.getItemServAdds())){
            datas.addAll(req.getItemServAdds());
        }
        if(!CollectionUtils.isEmpty(req.getItemServUpds())){
            datas.addAll(req.getItemServUpds());
        }
        ItemPublishType publishType = ItemPublishType.get(category.getCategoryFeature().getPublishType());
        ItemPubHelper pubHelper = itemPubHelperFactory.create(publishType);
        //属性验证
        pubHelper.validateForInsert(item, req.getGoods(), datas);
        itemId = item.getId();
        ItemTO dest = itemQryManager.getItemTO(itemId,true,true);
        //属性验证
        pubHelper.validateForUpdate(req, dest);
        //属性设置
        pubHelper.propertyForUpdate(req, dest);
        itemPubRepository.update(req.getItem(),req.getGoods(),req.getItemServAdds(),req.getItemServUpds(),req.getItemServDels());
        return itemId;
    }

    /**
     * 上架商品
     */
    public void publish(ItemPublishDTO param){
        ItemTO item = validation(param);
        if(!item.getItem().isCanPublish()){
            String errorMsg = String.format("item status:%d is can't publish!", item.getItem().getStatus());
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
        }
        itemPubRepository.updateStatus(item, ItemStatus.valid.getValue(), GoodsStatus.valid.getValue());

    }

    /**
     * 下架商品
     */
    public void close(ItemPublishDTO param){
        ItemTO item = validation(param);
        if(!item.getItem().isCanClose()){
            String errorMsg = String.format("item status:%d is can't unpub!", item.getItem().getStatus());
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
        }
        itemPubRepository.updateStatus(item, ItemStatus.invalid.getValue(), GoodsStatus.invalid.getValue());

    }

    /**
     * 更新状态的参数验证
     */
    private ItemTO validation(ItemPublishDTO param){
        if(param.getSellerId() == 0 || param.getItemId() == 0){
            String errorMsg = String.format("sellerId:%d or itemId:%d is not exit!", param.getSellerId(),param.getItemId());
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
        }
        ItemTO itemAgretion =  itemQryManager.getItemTO(param.getItemId(),true,true);
        if(itemAgretion == null){
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),"itemAgretion is error!");
        }
        ItemDO item = itemAgretion.getItem();
        GoodsDO goods = itemAgretion.getGoods();
        if(item == null || goods == null){
            String errorMsg = String.format("item:%d or goods is not exit!", param.getItemId());
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
        }
        //调用user-service获取商户信息，进行验证商户状态和权限；
        if(!vendorServiceProxy.checkSeller(item.getSellerId())){
            String errorMsg = String.format("sellerId[%d] is not right!", item.getSellerId());
            throw new ServiceException(ErrorCode.PARAM_ERROR.getErrorCode(),errorMsg);
        }
        return itemAgretion;
    }
}
