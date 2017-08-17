package com.jf.oauth.core.entity;

import org.apache.ibatis.type.Alias;

/**
 * 可信任的服务端信息
 CREATE TABLE IF NOT EXISTS `app_auth_trust` (
  id BIGINT NOT NULL AUTO_INCREMENT,
  ip VARCHAR(256) NULL comment '信任来源的IP地址',
  name VARCHAR(1024) NULL comment '信任源名',
  create_time TIMESTAMP NULL comment '创建时间',
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  data_status TINYINT(1) NULL DEFAULT 1 comment '数据状态:1-有效,0-无效',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET utf8;
 * @author qiph
 *
 */
@Alias("AppAuthTrust")
public class AppAuthTrust extends BaseEntity{
	protected long id;
	//信任源IP
	protected String ip;
	//信任源名
	protected String name;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
