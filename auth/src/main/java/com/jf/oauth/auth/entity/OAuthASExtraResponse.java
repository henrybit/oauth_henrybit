package com.jf.oauth.auth.entity;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;

/**
 * 扩展标准OAuthASResponse数据
 * @author qiph
 * @version 1.0
 */
public class OAuthASExtraResponse extends OAuthASResponse{
	//扩展的额外信息
	protected String extraInfo;
	
	protected OAuthASExtraResponse(String uri, int responseStatus) {
		super(uri, responseStatus);
	}
	
	public static OAuthTokenExtraResponseBuilder tokenExtraResponse(int code) {
		return new OAuthTokenExtraResponseBuilder(code);
	}
	
	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	
	public static class OAuthTokenExtraResponseBuilder extends OAuthResponseBuilder {
		
		public OAuthTokenExtraResponseBuilder(int responseCode) {
            super(responseCode);
        }
		
		public OAuthTokenExtraResponseBuilder setExtraInfo(String extraInfo) {
			this.parameters.put("extraInfo", extraInfo);
			return this;
		}

        public OAuthTokenExtraResponseBuilder setAccessToken(String token) {
            this.parameters.put(OAuth.OAUTH_ACCESS_TOKEN, token);
            return this;
        }

        public OAuthTokenExtraResponseBuilder setExpiresIn(String expiresIn) {
            this.parameters.put(OAuth.OAUTH_EXPIRES_IN, expiresIn == null ? null : Long.valueOf(expiresIn));
            return this;
        }

        public OAuthTokenExtraResponseBuilder setRefreshToken(String refreshToken) {
            this.parameters.put(OAuth.OAUTH_REFRESH_TOKEN, refreshToken);
            return this;
        }
        
        public OAuthTokenExtraResponseBuilder setTokenType(String tokenType) {
            this.parameters.put(OAuth.OAUTH_TOKEN_TYPE, tokenType);
            return this;
        }

        public OAuthTokenExtraResponseBuilder setParam(String key, String value) {
            this.parameters.put(key, value);
            return this;
        }

        public OAuthTokenExtraResponseBuilder location(String location) {
            this.location = location;
            return this;
        }
	}
}
