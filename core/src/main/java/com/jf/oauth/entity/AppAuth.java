package com.jf.oauth.entity;

import org.apache.ibatis.type.Alias;

/**
 * app授权信息<br>
 CREATE TABLE IF NOT EXISTS `app_auth` (
 id BIGINT NOT NULL AUTO_INCREMENT,
 name VARCHAR(1024) NULL comment '应用名',
 app_id VARCHAR(256) NULL comment 'APP唯一标识',
 app_secret VARCHAR(256) NULL comment 'APP的密钥',
 create_time TIMESTAMP NULL comment '创建时间',
 update_time TIMESTAMP NULL comment '更新时间',
 data_status TINYINT(1) NULL DEFAULT 1 comment '数据状态:1-有效,0-无效',
 PRIMARY KEY (`id`)
 )ENGINE = InnoDB CHARACTER SET utf8;
 * Created by henrybit on 2017/5/5.
 */
@Alias("AppAuth")
public class AppAuth extends BaseEntity{
    //主键ID
    protected long id;
    //应用名
    protected String name;
    //应用ID
    protected String appId;
    //应用密钥
    protected String appSecret;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
