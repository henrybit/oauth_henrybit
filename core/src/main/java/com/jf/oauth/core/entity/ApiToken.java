package com.jf.oauth.core.entity;

import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 其他系统API相关的Token
CREATE TABLE IF NOT EXISTS `api_token` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  client_id VARCHAR(256) NULL comment '客户端ID,标识调用的是哪个系统',
  user_name VARCHAR(1024) NULL comment '登陆账号',
  token VARCHAR(1024) NULL comment 'Token',
  token_expire_time TIMESTAMP NULL comment '口令到期时间',
  create_time TIMESTAMP NULL comment '创建时间',
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  data_status TINYINT(1) NULL DEFAULT 1 comment '数据状态:1-有效,0-无效',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET utf8;
 * @author qiph
 *
 */
@Alias("ApiToken")
public class ApiToken extends BaseEntity{
	protected long id;
	//客户端ID
	protected String clientId;
	//登陆账号
	protected String userName;
	//口令
	protected String token;
	//Token过期时间
	protected Date tokenExpireTime;
	//Token过期时间毫秒数
	protected long tokenExpireTimeLong;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getTokenExpireTime() {
		return tokenExpireTime;
	}
	public void setTokenExpireTime(Date tokenExpireTime) {
		this.tokenExpireTime = tokenExpireTime;
	}
	public long getTokenExpireTimeLong() {
		return tokenExpireTimeLong;
	}
	public void setTokenExpireTimeLong(long tokenExpireTimeLong) {
		this.tokenExpireTimeLong = tokenExpireTimeLong;
	}
}
