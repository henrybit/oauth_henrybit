package com.jf.oauth.core.entity;

import org.apache.ibatis.type.Alias;

/**
 * Auth用户密码模式实体<br>
CREATE TABLE IF NOT EXISTS `app_auth_password`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  auth_user VARCHAR(256) NULL comment '信任来源的IP地址',
  auth_password VARCHAR(256) NULL comment '信任来源的IP地址',
  name VARCHAR(1024) NULL comment '信任源名',
  create_time TIMESTAMP NULL comment '创建时间',
  update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  data_status TINYINT(1) NULL DEFAULT 1 comment '数据状态:1-有效,0-无效',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET utf8;
 * @author qiph
 * @version 1.0
 */
@Alias("AppAuthPassword")
public class AppAuthPassword extends BaseEntity{
	//主键ID
	protected long id;
	//用户
	protected String authUser;
	//密码
	protected String authPassword;
	//信任源
	protected String name;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAuthUser() {
		return authUser;
	}
	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}
	public String getAuthPassword() {
		return authPassword;
	}
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
