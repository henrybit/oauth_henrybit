package com.jf.oauth.core.entity;

import org.apache.ibatis.type.Alias;

/**
CREATE TABLE IF NOT EXISTS `api_token_server` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  client_id VARCHAR(256) NULL comment '客户端ID',
  name VARCHAR(1024) NULL comment '客户端名',
  url text NULL comment '接口URL地址',
  expire_time int default 3600 comment '失效时间:按秒算;默认3600秒.', 
  create_time TIMESTAMP NULL comment '创建时间',
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  data_status TINYINT(1) NULL DEFAULT 1 comment '数据状态:1-有效,0-无效',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET utf8;
 * @author qiph
 * version 1.0
 */
@Alias("ApiTokenServer")
public class ApiTokenServer extends BaseEntity{
	protected long id;
	//客户端ID
	protected String clientId;
	//客户端名
	protected String name;
	//接口地址
	protected String url;
	//失效时间（默认3600s）
	protected int expireTime;
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
}
