package com.jf.oauth.service.impl;

import com.jf.oauth.dao.AppAuthCodeDao;
import com.jf.oauth.dao.AppAuthDao;
import com.jf.oauth.entity.AppAuth;
import com.jf.oauth.entity.AppAuthCode;
import com.jf.oauth.entity.Constant;
import com.jf.oauth.entity.OauthConstant;
import com.jf.oauth.service.OauthService;
import com.jf.oauth.tools.StringTools;
import com.jf.oauth.tools.TimeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * 授权服务<br>
 * Created by henrybit on 2017/5/4.
 * @version 1.0
 */
@Service
public class OauthServiceImpl implements OauthService {

    @Autowired
    protected AppAuthDao appAuthDao;
    @Autowired
    protected AppAuthCodeDao appAuthCodeDao;

    public boolean checkClientId(String clientId) {
        if (StringTools.isEmpty(clientId))
            return false;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appId", clientId);
        AppAuth appAuth = this.appAuthDao.getOne(map);
        if (appAuth != null && appAuth.getDataStatus()== Constant.DATA_STATUS_VALID) {
            return true;
        }
        return false;
    }

    public boolean checkClientSecret(String clientId, String clientSecret) {
        if (StringTools.isEmpty(clientId) || StringTools.isEmpty(clientSecret))
            return false;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appId", clientId);
        map.put("appSecret", clientSecret);
        AppAuth appAuth = this.appAuthDao.getOne(map);
        if (appAuth != null && appAuth.getDataStatus()== Constant.DATA_STATUS_VALID) {
            return true;
        }
        return false;
    }

    public boolean checkAuthCode(String authCode) {
        if (StringTools.isEmpty(authCode))
            return false;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("authCode", authCode);
        AppAuthCode appAuthCode = this.appAuthCodeDao.getOne(map);
        if (appAuthCode != null) {
            Date codeExpireTime = appAuthCode.getCodeExpireTime();
            if (codeExpireTime!=null && codeExpireTime.getTime()>=System.currentTimeMillis())
                return true;
        }
        return false;
    }

    public boolean checkAccessToken(String accessToken) {
        if(StringTools.isEmpty(accessToken))
            return false;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("accessToken", accessToken);
        AppAuthCode appAuthCode = this.appAuthCodeDao.getOne(map);
        if (appAuthCode != null) {
            Date tokenExpireTime = appAuthCode.getTokenExpireTime();
            if (tokenExpireTime!=null && tokenExpireTime.getTime()>=System.currentTimeMillis())
                return true;
        }
        return false;
    }

    public AppAuthCode addAuthCode(String clientId, String authCode) {
        if (StringTools.isEmpty(clientId) || StringTools.isEmpty(authCode))
            return null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appId", clientId);
        AppAuth appAuth = this.appAuthDao.getOne(map);
        if (appAuth != null) {
            String clientSecret = appAuth.getAppSecret();
            AppAuthCode appAuthCode = new AppAuthCode();
            appAuthCode.setAppId(clientId);
            appAuthCode.setAppSecret(clientSecret);
            appAuthCode.setAuthCode(authCode);
            appAuthCode.setCodeExpireTime(TimeTools.addCurrentDateBySeconds(OauthConstant.AUTH_CODE_EXPIRE_TIME));
            appAuthCode.setCreateTime(TimeTools.getCurrentDate());
            appAuthCode.setUpdateTime(TimeTools.getCurrentDate());
            appAuthCode.setDataStatus(Constant.DATA_STATUS_VALID);

            int el = this.appAuthCodeDao.addOne(appAuthCode);
            if (el > 0) {
                return appAuthCode;
            }
        }
        return null;
    }

    @Override
    public AppAuthCode addAccessToken(String clientId, String authCode, String accessToken) {
        if (StringTools.isEmpty(clientId) || StringTools.isEmpty(authCode) || StringTools.isEmpty(accessToken))
            return null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appId", clientId);
        map.put("authCode", authCode);
        AppAuthCode appAuthCode = this.appAuthCodeDao.getOne(map);
        if (appAuthCode != null) {
            Date tokenExpireTime = TimeTools.addCurrentDateBySeconds(OauthConstant.ACCESS_TOKEN_EXPIRE_TIME);
            HashMap<String, Object> umap = new HashMap<String, Object>();
            umap.put("accessToken", accessToken);
            umap.put("tokenExpireTime", tokenExpireTime);
            umap.put("authCode", authCode);
            int el = this.appAuthCodeDao.updateOne(umap);
            if (el > 0) {
                appAuthCode.setAccessToken(accessToken);
                appAuthCode.setTokenExpireTime(tokenExpireTime);
                return appAuthCode;
            }
        }
        return null;
    }
}
