package com.jf.oauth.core.entity;

/**
 * Oauth授权常量<br>
 * Created by henrybit on 2017/5/5.
 * @since 2017/05/05
 */
public class OauthConstant {

    /**授权码过期时间：300秒-5分钟*/
    public final static int AUTH_CODE_EXPIRE_TIME = 300;
    /**AccessToken过期时间：2小时-7200秒*/
    public final static int ACCESS_TOKEN_EXPIRE_TIME = 7200;


    /**授权相关提示信息 start**/
    public final static String INVALID_CLIENT_ID = "无效的客户端ID";
    public final static String ERROR_CLIENT_SECRET = "无效的客户端密钥";
    public final static String INVALID_CALLBACK_URL = "客户端需需要提供OAuth回调地址";
    public final static String GET_AUTH_CODE_FAILED = "获取授权code失败";
    public final static String ERROR_AUTH_CODE_FAILED = "错误的授权code";
    public final static String NO_SUPPORT_GRANT_TYPE = "暂不支持的授权类型";
    public final static String GET_ACCESS_TOKEN_FAILED = "获取AccessToken失败";
    public final static String GRANT_TYPE_ERROR = "授权类型错误";
    
    public final static int TOKEN_IS_NULL = 40001;
    public final static String TOKEN_IS_NULL_DESC = "Token为空";
    public final static int TOKEN_ILLEGAL = 40002;
    public final static String TOKEN_ILLEGAL_DESC = "Token无效";
    public final static int TOKEN_CHECK_ERROR = 40003;
    public final static String TOKEN_CHECK_ERROR_DESC = "Token检验异常";
    /**授权相关提示信息 end**/
    
}
