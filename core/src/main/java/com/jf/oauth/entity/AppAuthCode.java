package com.jf.oauth.entity;

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * App授权码(code与accessToken)<br>
 CREATE TABLE IF NOT EXISTS `app_auth_code` (
 id BIGINT NOT NULL AUTO_INCREMENT,
 app_id VARCHAR(256) NULL comment 'APP唯一标识',
 app_secret VARCHAR(256) NULL comment 'APP的密钥',
 auth_code VARCHAR(1024) NULL comment '授权码',
 code_expire_time TIMESTAMP NULL comment '授权码到期时间',
 access_token VARCHAR(1024) NULL comment '访问口令',
 token_expire_time TIMESTAMP NULL comment '口令到期时间',
 create_time TIMESTAMP NULL comment '创建时间',
 update_time TIMESTAMP NULL comment '更新时间',
 data_status TINYINT(1) NULL DEFAULT 1 comment '数据状态:1-有效,0-无效',
 PRIMARY KEY (`id`)
 )ENGINE = InnoDB CHARACTER SET utf8;
 * Created by henrybit on 2017/5/5.
 * @version 1.0
 */
@Alias("AppAuthCode")
public class AppAuthCode extends BaseEntity{
    protected long id;
    //APP唯一标识
    protected String appId;
    //APP的密钥
    protected String appSecret;
    //授权码
    protected String authCode;
    //授权码到期时间
    protected Date codeExpireTime;
    //访问口令
    protected String accessToken;
    //口令到期时间
    protected Date tokenExpireTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Date getCodeExpireTime() {
        return codeExpireTime;
    }

    public void setCodeExpireTime(Date codeExpireTime) {
        this.codeExpireTime = codeExpireTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(Date tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }
}
