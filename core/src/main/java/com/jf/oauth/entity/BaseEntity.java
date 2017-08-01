package com.jf.oauth.entity;

import java.util.Date;

/**
 * 基础实体类
 * Created by henrybit on 2017/5/5.
 * @version 1.0
 */
public class BaseEntity {
    //创建时间
    protected Date createTime;
    //更新时间
    protected Date updateTime;
    //数据状态：1-有效，0-无效
    protected int dataStatus;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }
}
