package com.jf.oauth.service;

import com.jf.oauth.entity.AppAuthCode;

/**
 * 授权服务<br>
 * Created by henrybit on 2017/5/4.
 * @version 1.0
 */
public interface OauthService {

    /**
     * 验证请求中的client_id是否合法有效
     * @param clientId 客户端ID
     * @return boolean-true or false
     */
    boolean checkClientId(String clientId);

    /**
     * 验证客户端密钥是否有效
     * @param clientId 客户端ID
     * @param clientSecret 客户端密钥
     * @return boolean-true or false
     */
    boolean checkClientSecret(String clientId, String clientSecret);

    /**
     * 验证授权码是否有效
     * @param authCode 授权码
     * @return boolean-true or false
     */
    boolean checkAuthCode(String authCode);

    /**
     * 验证Token是否有效
     * @param accessToken token
     * @return boolean-true or false
     */
    boolean checkAccessToken(String accessToken);

    /**
     * 创建一个授权码
     * @param clientId 客户端ID
     * @param authCode 授权码
     * @return AppAuthCode-授权码实体
     */
    AppAuthCode addAuthCode(String clientId, String authCode);

    /**
     * 创建一个Token
     * @param clientId 客户端ID
     * @param authCode 授权码
     * @param accessToken 访问口令
     * @return AppAuthCode-访问口令实体
     */
    AppAuthCode addAccessToken(String clientId, String authCode, String accessToken);
}
