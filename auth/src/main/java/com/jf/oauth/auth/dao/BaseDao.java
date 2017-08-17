package com.jf.oauth.auth.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by henrybit on 2016/11/28.
 * @version 1.0
 */
public interface BaseDao<T> {
    /**
     * 获取一个实体
     * @param map
     * @return T
     */
    T getOne(Map map);

    /**
     * 获取一个集合
     * @param map
     * @return list
     */
    List<T> getList(Map map);

    /**
     * 更新一个实体
     * @param map
     * @return int-受影响的记录数
     */
    int updateOne(Map map);

    /**
     * 逻辑删除一个实体
     * @param map
     * @return int-受影响的记录数
     */
    int deleteOne(Map map);

    /**
     * 物理删除一个实体
     * @param map
     * @return int-受影响的记录数
     */
    int dropOne(Map map);

    /**
     * 新增一个实体
     * @param t
     * @return int-受影响的记录数
     */
    int addOne(T t);

    /**
     * 获取某个条件下的数据条数
     * @param map
     * @return int-数据条数
     */
    int getCount(Map map);
}
