package me.j360.dubbo.common;


import me.j360.dubbo.base.model.domian.BaseModel;
import me.j360.dubbo.base.model.domian.BaseQuery;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<M extends BaseModel<ID>, ID extends Serializable, Q extends BaseQuery> {

    /**
     * 增加
     *
     * @param m
     */
    void add(M m);

    /**
     * 删除
     *
     * @param m
     */
    void delete(M m);

    /**
     * 更新
     *
     * @param m
     */
    void update(M m);

    /**
     * 获取
     *
     * @param id 唯一标识
     * @return
     */
    M get(ID id);

    /**
     * 查询
     *
     * @param ids
     * @return
     */
    List<M> findIn(List<ID> ids);

    /**
     * 查询
     *
     * @param query 查询参数
     * @return
     */
    List<M> query(Q query);



    Long getCount(String countColumn, Long id);

}
