package me.j360.dubbo.repository;

import me.j360.dubbo.api.constant.ErrorCode;
import me.j360.dubbo.base.exception.RepositoryException;
import me.j360.dubbo.base.model.result.PageDO;
import me.j360.dubbo.dao.mapper.UserMapper;
import me.j360.dubbo.dao.model.UserDO;
import me.j360.dubbo.exception.ArgumentException;
import me.j360.dubbo.util.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package: me.j360.dubbo.dao.repository
 * User: min_xu
 * Date: 16/8/23 下午2:57
 * 说明：
 */

@Repository
public class UserRepository {

    @Autowired
    private UserMapper userMapper;

    public PageDO<List<UserDO>> pageList(int pageNo, int pageSize){
        PageDO<List<UserDO>> page = new PageDO<List<UserDO>>();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        try{
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("pageNo", (pageNo-1)*pageSize);
            param.put("pageSize", pageSize);

           /* List<UserDO> datas = userMapper.pageList(param);
            int recordCount = userMapper.pageCount(param);
            int recordSize  = datas == null?0:datas.size();
            page.setRecordCount(recordCount);
            page.setRecordSize(recordSize);
            page.setData(datas);*/
            return page;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }
    }


    public UserDO getGoods(long itemId) {
        try{
            Params params = new Params("itemId",itemId);
            /*List<UserDO> goods = userMapper.getByIdDyn(params.getResult());
            if(CollectionUtils.isEmpty(goods)){
                throw new ArgumentException(ErrorCode.GOODS_FIND_ERROR.getErrorCode(),ErrorCode.GOODS_FIND_ERROR.getErrorMsg());
            }
            if(goods.size() > 1){
                throw new ArgumentException(ErrorCode.GOODS_FIND_SIZE_ERROR.getErrorCode(),ErrorCode.GOODS_FIND_SIZE_ERROR.getErrorMsg());
            }
            UserDO data = goods.get(0);
            return data;*/
            return null;
        }catch(ArgumentException ex){
            throw ex;
        }catch(Throwable th){
            throw new RepositoryException(ErrorCode.DB_ERROR.getErrorCode(),ErrorCode.DB_ERROR.getErrorMsg(),th);
        }
    }

    public void create(UserDO userDO) {
        userMapper.add(userDO);
    }

    public int count() {
        return userMapper.count();
    }
}
