package com.jf.oauth.auth.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jf.oauth.auth.entity.OAuthASExtraResponse;
import com.jf.oauth.auth.service.OauthService;
import com.jf.oauth.core.entity.AppAuthCode;
import com.jf.oauth.core.entity.OauthConstant;
import com.jf.oauth.core.tools.NumberTools;
import com.jf.oauth.core.tools.RequestTools;
import com.jf.oauth.core.tools.SecurityTools;
import com.jf.oauth.core.tools.Sha1Tools;
import com.jf.oauth.core.tools.StringTools;

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
     * 范例：http://172.25.6.24:8080/auth/authorize/code?client_id=123456&response_type=code&redirect_uri=http://172.25.6.24:8080/auth/authorize/accesstoken
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
    	HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("text","html",Charset.forName("utf-8"));
        headers.setContentType(mediaType);
        
        String redirectURI = "";
        OAuthResponse failedResponse = null;
        OAuthResponse successResponse = null;
        try {
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String clientId = oauthRequest.getClientId();
            String encryCode = oauthRequest.getParam("code"); //自定义授权码
            //检查clientId是否有效
            boolean clientIdValid = oauthService.checkClientId(clientId);

            if (!clientIdValid) {
                //无效的客户端ID
                failedResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OauthConstant.INVALID_CLIENT_ID)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(),headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }

            String customCode = "";
            if (StringTools.isNotEmpty(encryCode)) {
            	String decryCode = SecurityTools.decode(encryCode); //自定义code+";"+时间戳(毫秒数)
            	if (StringTools.isNotEmpty(decryCode)) {
            		String[] array = decryCode.split(";");
            		if (array!=null && array.length==2) {
            			long codeTime = NumberTools.parseLong(array[1]);
                		long diffTime = Math.abs(System.currentTimeMillis()-codeTime);
                		if (diffTime <= 300000) //超过5分钟的无效
                			customCode = array[0];
            		}
            	}
            }
            //获取授权码
            String authorizationCode = customCode; //如果有自定义授权码，优先采用自定义授权码
            if (StringTools.isEmpty(authorizationCode)) {
	            String responseType = request.getParameter(OAuth.OAUTH_RESPONSE_TYPE);
	            if (responseType.equals(ResponseType.CODE.toString())) {
	                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
	                authorizationCode = oAuthIssuer.authorizationCode();
	            }
            }

            //向DB中写入authorizationCode
            AppAuthCode appAuthCode = this.oauthService.addAuthCode(clientId, authorizationCode);
            if (appAuthCode == null)
                throw new Exception("创建AuthCode失败");

            //得到到客户端重定向地址
            redirectURI = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
            //进行OAuth响应构建
            successResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_OK)
                    .setCode(authorizationCode)
                    .location(redirectURI)
                    .buildJSONMessage();
            //根据OAuthResponse返回ResponseEntity响应
            headers.setLocation(new URI(successResponse.getLocationUri()));
            return new ResponseEntity(successResponse, headers, HttpStatus.valueOf(successResponse.getResponseStatus()));
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            if (OAuthUtils.isEmpty(redirectURI)) {//告诉客户端没有传入redirectUri直接报错
                failedResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                        .setErrorDescription(OauthConstant.INVALID_CALLBACK_URL)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(),headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
            else {
                //返回错误消息（如?error=)
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.CodeResponse.INVALID_REQUEST)
                        .setErrorDescription(e.getError())
                        .location(redirectURI)
                        .buildJSONMessage();
                headers.setLocation(new URI(failedResponse.getLocationUri()));
                return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
        } catch (Exception e) {
        	e.printStackTrace();
            failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .setError(OAuthError.OAUTH_ERROR)
                    .setErrorDescription(OauthConstant.GET_AUTH_CODE_FAILED)
                    .location(redirectURI)
                    .buildJSONMessage();
            headers.setLocation(new URI(failedResponse.getLocationUri()));
            return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
        }
    }

    /**
     * 授权码模式<br>
     * http://172.25.6.24:8080/auth/authorize/accesstoken?code=dfc33848b6f88a72933434f4fefdc0f6&grant_type=authorization_code&client_id=123456&client_secret=83fii819&response_type=code-oauth&redirect_uri=http://172.25.6.24:8080/auth/authorize/accesstoken
     * 必须是POST请求，同时Content-Type必须是application/x-www-form-urlencoded
     * <span>request header</span>
     * <ul>
     *     <li>Authorization-授权内容(数组中第一位为client_id，优先采用这里的client_id，如果为空采用request param中的client_id；第二位为client_secret，优先采用这里的，如果Authorization中为空，采用request param中的client_secret)</li>
     * </ul>
     * <span>request param:授权码模式</span>
     * 请求参数：
     * <ul>
     *     <li>grant_type-授权类型-选填（取值为authorization_code）</li>
     *     <li>client_id-客户端ID-必填</li>
     *     <li>client_secret-客户端密钥-必填</li>
     *     <li>code-授权码-必填</li>
     *     <li>response_type=code-oauth类型：授权码-必填</li>
     *     <li>extra_info-额外信息</li>
     * </ul>
     * <span>客户端证书模式（服务授信模式）</span>
     * 请求头：
     * <ul>
     * 	   <li>auth-x-clients-加密client</li>
     * </ul>
     * 请求参数：
     * <ul>
     *     <li>grant_type-授权类型-选填（取值为client_credentials）</li>
     *     <li>client_id-客户端ID-必填</li>
     *     <li>client_secret-客户端密钥-必填</li>
     *     <li>extra_info-额外信息</li>
     * </ul>
     * @param request
     * @param response
     * @return
     * @throws OAuthSystemException
     */
    @RequestMapping(value="/accesstoken")
    public Object accessToken(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException {
    	HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("text","html",Charset.forName("utf-8"));
        headers.setContentType(mediaType);
        
        OAuthResponse failedResponse = null;
        OAuthResponse successResponse = null;
        try {
            //构建OAuth请求
            String clientId = request.getParameter(OAuth.OAUTH_CLIENT_ID);
            String clientSecret = request.getParameter(OAuth.OAUTH_CLIENT_SECRET);
            String grantType = request.getParameter(OAuth.OAUTH_GRANT_TYPE);
            String extraInfo = request.getParameter("extra_info");
            String authCode = ""; //授权码（根据不同grantType来提取授权码）
            //授权类型不能为空
            if (StringTools.isEmpty(grantType)) {
            	failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OauthConstant.GRANT_TYPE_ERROR)
                        .buildJSONMessage();
            	return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
            
            //检查提交的客户端id是否正确
            if (!oauthService.checkClientId(clientId)) {
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OauthConstant.INVALID_CLIENT_ID)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }

            // 检查客户端安全KEY是否正确
            if (!oauthService.checkClientSecret(clientId, clientSecret)) {
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(OauthConstant.ERROR_CLIENT_SECRET)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
            
            if (GrantType.AUTHORIZATION_CODE.toString().equals(grantType)) { //授权码模式
                //验证不通过
            	authCode = request.getParameter(OAuth.OAUTH_CODE);
                if (!oauthService.checkAuthCode(authCode)) {
                    failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription(OauthConstant.ERROR_AUTH_CODE_FAILED)
                            .buildJSONMessage();
                    return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
                }
            } else if (GrantType.CLIENT_CREDENTIALS.toString().equals(grantType)) { //客户端模式（服务器互信模式）
            	//生成一个OauthCode（授信后才可以分配AuthCode）
            	String ip = RequestTools.getRealIp(request);
            	boolean isTrust = this.oauthService.checkTrustServer(ip);
            	if (isTrust) {
            		OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            		authCode = oAuthIssuer.authorizationCode();
            	}
            } else if (GrantType.IMPLICIT.toString().equals(grantType)) { //简易模式
            	//生产一个OauthCode
            	String sha1 = request.getHeader("jf-ot-info");
            	String nsha1 = Sha1Tools.encode(clientId+";"+clientSecret);
            	if (StringTools.isNotEmpty(sha1) && StringTools.isNotEmpty(nsha1) && sha1.equals(nsha1)) {
            		OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            		authCode = oAuthIssuer.authorizationCode();
            	}
            } else if (GrantType.PASSWORD.toString().equals(grantType)) { //账号与密码模式
            	//生产一个OauthCode
            	String user = request.getParameter("user");
            	String password = request.getParameter("pd");
            	boolean isOk = this.oauthService.checkAuthUserPassword(user, password);
            	if (isOk) {
            		OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            		authCode = oAuthIssuer.authorizationCode();
            	}
            } else { //其他暂时不支持的授权协议
                failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE)
                        .setErrorDescription(OauthConstant.NO_SUPPORT_GRANT_TYPE)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }

            //授权码异常
            if (StringTools.isEmpty(authCode)) {
            	failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                        .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                        .setErrorDescription(OauthConstant.ERROR_AUTH_CODE_FAILED)
                        .buildJSONMessage();
                return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
            
            //生成AccessToken
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();

            //向DB中插入AccessToken并附带过期时间
            AppAuthCode appAuthCode = this.oauthService.addAccessToken(clientId, clientSecret, authCode, accessToken, grantType, extraInfo);
            if (appAuthCode == null)
                throw new Exception("创建Token失败");

            //生成OAuth响应(Token过期时间单位是秒)
            successResponse = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(OauthConstant.ACCESS_TOKEN_EXPIRE_TIME))
                    .buildJSONMessage();

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(successResponse.getBody(), headers, HttpStatus.valueOf(successResponse.getResponseStatus()));
        } catch (OAuthProblemException e) {
        	e.printStackTrace();
            //构建错误响应
            failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION)
                    .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                    .setErrorDescription(e.getError())
                    .buildJSONMessage();
            return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
        } catch (Exception e) {
            failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .setError(OAuthError.OAUTH_ERROR)
                    .setErrorDescription(OauthConstant.GET_ACCESS_TOKEN_FAILED)
                    .buildJSONMessage();
            return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
        }
    }
    
   
    /**
     * 范例：http://172.25.6.24:8080/auth/authorize/checktoken?access_token=742735c987a88023dd9935526fe33c11
    * <span>request header</span>
     * <ul>
     *     <li>Authorization-授权相关信息(数组中第一位为client_id，优先采用这里的client_id，如果Authorization为空，采用request param中的client_id)</li>
     * </ul>
     * <span>request param</span>
     * <ul>
     *     <li>access_token-需要检查的Token-必填</li>
     * </ul>
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/checktoken")
    public Object checkAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("text","html",Charset.forName("utf-8"));
        headers.setContentType(mediaType);
        
    	OAuthResponse failedResponse = null;
        OAuthResponse successResponse = null;
    	try {
            //构建OAuth请求
            //OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            //String accessToken = oauthRequest.getParam(OAuth.OAUTH_ACCESS_TOKEN);
            String accessToken = request.getParameter(OAuth.OAUTH_ACCESS_TOKEN);
            if (StringTools.isEmpty(accessToken)) {
            	failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .setError(OauthConstant.TOKEN_IS_NULL+"")
                        .setErrorDescription(OauthConstant.TOKEN_IS_NULL_DESC)
                        .buildJSONMessage();
            	return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
            AppAuthCode appAuthCode = this.oauthService.checkAccessTokenExpire(accessToken);
            if (appAuthCode!=null && appAuthCode.isValid()) {
            	Date expireDate = appAuthCode.getTokenExpireTime();
            	String extraInfo = appAuthCode.getExtraInfo();
            	//(Token过期时间单位是秒)
            	if (expireDate != null) {
	            	long expireTime = expireDate.getTime();
	            	long nowTime = System.currentTimeMillis();
	            	long expiresIn = (expireTime-nowTime)/1000;
	            	if (expiresIn<=0 && expireTime>nowTime)
	            		expiresIn = 1;
		            successResponse = OAuthASExtraResponse.tokenExtraResponse(HttpServletResponse.SC_OK)
		                    .setAccessToken(accessToken)
		                    .setExpiresIn(String.valueOf(expiresIn))
		                    .setExtraInfo(extraInfo)
		                    .buildJSONMessage();
            	} else {
            		//Token的失效时间为null时，相当于永久不失效，增加扩展字段extra_info
            		successResponse = OAuthASExtraResponse.tokenExtraResponse(HttpServletResponse.SC_OK)
		                    .setAccessToken(accessToken)
		                    .setExpiresIn(String.valueOf(-1))
		                    .setExtraInfo(extraInfo)
		                    .buildJSONMessage();
            	}
            	return new ResponseEntity(successResponse.getBody(), headers, HttpStatus.valueOf(successResponse.getResponseStatus()));
            } else {
	        	//Token无效（过期或无法找到该token）
	        	failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
	                    .setError(OauthConstant.TOKEN_ILLEGAL+"")
	                    .setErrorDescription(OauthConstant.TOKEN_ILLEGAL_DESC)
	                    .buildJSONMessage();
	        	return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
            }
    	} catch (Exception e) {
    		e.printStackTrace();
    		failedResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .setError(OauthConstant.TOKEN_CHECK_ERROR+"")
                    .setErrorDescription(OauthConstant.TOKEN_CHECK_ERROR_DESC)
                    .buildJSONMessage();
    		return new ResponseEntity(failedResponse.getBody(), headers, HttpStatus.valueOf(failedResponse.getResponseStatus()));
    	}
    }
}
