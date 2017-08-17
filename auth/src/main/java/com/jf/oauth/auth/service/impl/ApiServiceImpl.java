package com.jf.oauth.auth.service.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jf.oauth.auth.dao.ApiTokenDao;
import com.jf.oauth.auth.dao.ApiTokenServerDao;
import com.jf.oauth.auth.service.ApiService;
import com.jf.oauth.core.entity.ApiToken;
import com.jf.oauth.core.entity.ApiTokenServer;
import com.jf.oauth.core.entity.Constant;
import com.jf.oauth.core.entity.response.EasOriResponse;
import com.jf.oauth.core.tools.HttpTools;
import com.jf.oauth.core.tools.JSONTools;
import com.jf.oauth.core.tools.SecurityApiTools;
import com.jf.oauth.core.tools.StringTools;
import com.jf.oauth.core.tools.TimeTools;

/**
 * 接口相关服务<br>
 * @author qiph
 * @version 1.0
 */
@Service
public class ApiServiceImpl implements ApiService{

	@Autowired
	private ApiTokenDao apiTokenDao;
	@Autowired
	private ApiTokenServerDao apiTokenServerDao;
	
	@Override
	public ApiTokenServer getApiInfo(String clientId) {
		if (StringTools.isEmpty(clientId))
			return null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("client_id", clientId);
		ApiTokenServer apiTokenServer = this.apiTokenServerDao.getOne(map);
		return apiTokenServer;
	}

	@Override
	public ApiToken getToken(String clientId, String userName) {
		if (StringTools.isEmpty(clientId) || StringTools.isEmpty(userName))
			return null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("clientId", clientId);
		map.put("userName", userName);
		map.put("expireTime", TimeTools.getCurrentDate());
		map.put("dataStatus", Constant.DATA_STATUS_VALID);
		ApiToken apiToken = this.apiTokenDao.getOne(map);
		if (apiToken == null) {
			//无法找到有效的Token，请求接口提取新Token，并写到DB中
			apiToken = createToken(clientId, userName);
			if (apiToken == null)
				return null;
			int el = this.apiTokenDao.addOne(apiToken);
			if (el > 0)
				return apiToken;
		} else {
			if (apiToken.getTokenExpireTime() != null)
				apiToken.setTokenExpireTimeLong(apiToken.getTokenExpireTime().getTime());
		}
		return apiToken;
	}

	@Override
	public ApiToken createToken(String clientId, String userName) {
		if (StringTools.isEmpty(clientId))
			return null;
		HashMap<String, Object> serverMap = new HashMap<String, Object>();
		serverMap.put("clientId", clientId);
		serverMap.put("dataStatus", Constant.DATA_STATUS_VALID);
		
		ApiTokenServer apiTokenServer = this.apiTokenServerDao.getOne(serverMap);
		if (apiTokenServer == null)
			return null;
		String url = apiTokenServer.getUrl();
		int expireTime = apiTokenServer.getExpireTime(); //单位秒
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		//parameters.put("clientid", clientId);
		//parameters.put("username", userName);
		//时间戳+";"+clientId+";"+登陆账号（传给金蝶的加密数据）
		String ori = System.currentTimeMillis()+";"+Constant.EAS_CLIENT_ID+";"+userName;
		parameters.put("ecrinfo", SecurityApiTools.encodeEas(ori));
		String response = HttpTools.post(url, parameters);
		
		//解析报文response，提取token
		EasOriResponse easOriResponse = JSONTools.parserJson(response, EasOriResponse.class);
		String easAccessToken = "";
		try {
			easAccessToken = easOriResponse.getData().getAccessToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date tokenExpireTime = TimeTools.addCurrentDateBySeconds(expireTime);
		if (tokenExpireTime == null)
			tokenExpireTime = TimeTools.getCurrentDate();
		ApiToken apiToken = new ApiToken();
		apiToken.setClientId(clientId);
		apiToken.setCreateTime(TimeTools.getCurrentDate());
		apiToken.setDataStatus(Constant.DATA_STATUS_VALID);
		apiToken.setToken(easAccessToken);
		apiToken.setTokenExpireTime(tokenExpireTime);
		apiToken.setTokenExpireTimeLong(tokenExpireTime.getTime());
		apiToken.setUpdateTime(TimeTools.getCurrentDate());
		apiToken.setUserName(userName);
		return apiToken;
	}
	
}
