create database oauth DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use oauth;

#应用授权信息
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


#应用的授权码和访问口令
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