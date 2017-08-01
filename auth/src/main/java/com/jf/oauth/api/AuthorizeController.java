package com.jf.oauth.api;

import com.jf.oauth.entity.AppAuthCode;
import com.jf.oauth.entity.OauthConstant;
import com.jf.oauth.service.OauthService;
import com.jf.oauth.tools.JSONTools;
import com.jf.oauth.tools.StringTools;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * 鉴权服务<br>
 * Created by henrybit on 2017/5/3.
 * @version 1.0
 * @since 2017/05/04
 */
@RestController
@RequestMapping(value="/authorize")
public class AuthorizeController {

    @Autowired
    private OauthService oauthService;

    /**
     * <span>request header</span>
     * <ul>
     *     <li>Authorization-授权相关信息(数组中第一位为client_id，优先采用这里的client_id，如果Authorization为空，采用request param中的client_id)</li>
     * </ul>
     * <span>request param</span>
     * <ul>
     *     <li>redirect_uri-重定向的URL-必填</li>
     *     <li>client_id-客户端ID-必填</li>
     *     <li>response_type=code-oauth类型：授权码-必填</li>
     * </ul>
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/code")
    public Object authorizeCode(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, URISyntaxException {
        String redirectURI = "";
        OAuthResponse failedResponse = null;
        OAuthResponse successResponse = null;
        try {
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String clientId = oauthRequest.getClientId();
            //检查clientId是否有效
            boolean clientIdValid = oauthService.checkClientId(clientId);

            if (!clientIdValid) {
                //无效的客户端ID
                failedResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OauthConstant.INVALID_CLIENT_ID)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }

            //获取授权码
            String authorizationCode = "";
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oAuthIssuer.authorizationCode();
            }

            //向DB中写入authorizationCode
            AppAuthCode appAuthCode = this.oauthService.addAuthCode(clientId, authorizationCode);
            if (appAuthCode == null)
                throw new Exception("创建AuthCode失败");

            //得到到客户端重定向地址
            redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            //进行OAuth响应构建
            successResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_OK)
                    .setCode(authorizationCode)
                    .location(redirectURI)
                    .buildJSONMessage();
            //根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(successResponse.getLocationUri()));
            return new ResponseEntity(successResponse, headers, HttpStatus.valueOf(successResponse.getResponseStatus()));
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            if (OAuthUtils.isEmpty(redirectURI)) {//告诉客户端没有传入redirectUri直接报错
                failedResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                        .setErrorDescription(OauthConstant.INVALID_CALLBACK_URL)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
            else {
                //返回错误消息（如?error=)
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                        .setErrorDescription(e.getError())
                        .location(redirectURI)
                        .buildJSONMessage();
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(new URI(failedResponse.getLocationUri()));
                return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
        } catch (Exception e) {
            failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .setError(OAuthError.OAUTH_ERROR)
                    .setErrorDescription(OauthConstant.GET_AUTH_CODE_FAILED)
                    .location(redirectURI)
                    .buildJSONMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(failedResponse.getLocationUri()));
            return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
        }
    }

    /**
     * <span>request header</span>
     * <ul>
     *     <li>Authorization-授权内容(数组中第一位为client_id，优先采用这里的client_id，如果为空采用request param中的client_id；第二位为client_secret，优先采用这里的，如果Authorization中为空，采用request param中的client_secret)</li>
     * </ul>
     * <span>request param:</span>
     * <ul>
     *     <li>grant_type-授权类型-选填（目前只支持authorization_code）</li>
     *     <li>client_id-客户端ID-必填</li>
     *     <li>client_secret-客户端密钥-必填</li>
     *     <li>code-授权码-必填</li>
     *     <li>redirect_uri-重定向的URL-必填</li>
     *     <li>response_type=code-oauth类型：授权码-必填</li>
     * </ul>
     * @param request
     * @param response
     * @return
     * @throws OAuthSystemException
     */
    @RequestMapping(value="/accesstoken")
    public Object accessToken(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException {
        OAuthResponse failedResponse = null;
        OAuthResponse successResponse = null;
        try {
            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            String clientId = oauthRequest.getClientId();
            String clientSecret = oauthRequest.getClientSecret();
            String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            String grantType = oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE);

            //检查提交的客户端id是否正确
            if (!oauthService.checkClientId(clientId)) {
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OauthConstant.INVALID_CLIENT_ID)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }

            // 检查客户端安全KEY是否正确
            if (!oauthService.checkClientSecret(clientId, clientSecret)) {
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(OauthConstant.ERROR_CLIENT_SECRET)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }


            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            if (StringTools.isNotEmpty(grantType) && grantType.equals(GrantType.AUTHORIZATION_CODE.toString())) {
                //验证不通过
                if (!oauthService.checkAuthCode(authCode)) {
                    failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription(OauthConstant.ERROR_AUTH_CODE_FAILED)
                            .buildJSONMessage();
                    return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
                }
            } else if (StringTools.isEmpty(grantType)) {
                //验证不通过
                if (!oauthService.checkAuthCode(authCode)) {
                    failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription(OauthConstant.ERROR_AUTH_CODE_FAILED)
                            .buildJSONMessage();
                    return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
                }
            } else {
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.INVALID_GRANT)
                        .setErrorDescription(OauthConstant.NO_SUPPORT_GRANT_TYPE)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }

            //生成AccessToken
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();

            //向DB中插入AccessToken并附带过期时间
            AppAuthCode appAuthCode = this.oauthService.addAccessToken(clientId, authCode, accessToken);
            if (appAuthCode == null)
                throw new Exception("创建Token失败");

            //生成OAuth响应
            successResponse = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(OauthConstant.ACCESS_TOKEN_EXPIRE_TIME))
                    .buildJSONMessage();

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(successResponse.getBody(), HttpStatus.valueOf(successResponse.getResponseStatus()));
        } catch (OAuthProblemException e) {
            //构建错误响应
            failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                    .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                    .setErrorDescription(e.getError())
                    .buildJSONMessage();
            return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
        } catch (Exception e) {
            failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .setError(OAuthError.OAUTH_ERROR)
                    .setErrorDescription(OauthConstant.GET_ACCESS_TOKEN_FAILED)
                    .buildJSONMessage();
            return new ResponseEntity(failedResponse.getBody(), HttpStatus.valueOf(failedResponse.getResponseStatus()));
        }
    }
}
