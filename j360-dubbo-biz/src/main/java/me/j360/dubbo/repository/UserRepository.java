package me.j360.dubbo.repository;

import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.exception.ArgumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package: me.j360.dubbo.dao.repository
 * User: min_xu
 * Date: 16/8/23 下午2:57
 * 说明：
 */
public class UserRepository {

    @Autowired
    private CategoryMapper categoryMapper;

    public PageDO<List<CategoryDO>> pageList(int pageNo,int pageSize){
        PageDO<List<CategoryDO>> page = new PageDO<List<CategoryDO>>();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        try{
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("pageNo", (pageNo-1)*pageSize);
            param.put("pageSize", pageSize);

            List<CategoryDO> datas = categoryMapper.pageList(param);
            int recordCount = categoryMapper.pageCount(param);
            int recordSize  = datas == null?0:datas.size();
            page.setRecordCount(recordCount);
            page.setRecordSize(recordSize);
            page.setData(datas);
            return page;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }
    }


    public GoodsDO getGoods(long itemId) {
        try{
            Params params = new Params("itemId",itemId);
            List<GoodsDO> goods = goodsMapper.getByIdDyn(params.getResult());
            if(CollectionUtils.isEmpty(goods)){
                throw new ArgumentException(ErrorCode.GOODS_FIND_ERROR.getErrorCode(),ErrorCode.GOODS_FIND_ERROR.getErrorMsg());
            }
            if(goods.size() > 1){
                throw new ArgumentException(ErrorCode.GOODS_FIND_SIZE_ERROR.getErrorCode(),ErrorCode.GOODS_FIND_SIZE_ERROR.getErrorMsg());
            }
            GoodsDO data = goods.get(0);
            return data;
        }catch(ArgumentException ex){
            throw ex;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }
    }

}
