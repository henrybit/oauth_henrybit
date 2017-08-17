package com.jf.oauth.auth.service;

import com.jf.oauth.core.entity.ApiToken;
import com.jf.oauth.core.entity.ApiTokenServer;

/**
 * 其他系统API调用服务相关
 * @author qiph
 * @version 1.0
 */
public interface ApiService {
	/**
	 * 根据调用者提供的clientID获取接口信息
	 * @param clientId
	 * @return ApiTokenServer
	 */
	ApiTokenServer getApiInfo(String clientId);
	
	/**
	 * 通过clientId获取一个有效的Token
	 * @param clientId
	 * @param userName
	 * @return ApiToken
	 */
	ApiToken getToken(String clientId, String userName);
	
	/**
	 * 创建一个有效的Token
	 * 该Token来源于源端系统（需要通过接口请求获取）
	 * @param clientId
	 * @param userName
	 * @return ApiToken
	 */
	ApiToken createToken(String clientId, String userName);
}
