package com.jf.oauth.core.entity.response;

/**
 * 报文
{
    "_cid": "a4b5a28a-cc71-4138-b760-a42ff1e08c52",
    "_key": "2d01d532-fc3e-4dca-8789-02b38aab0783",
    "code": null,
    "data": {
        "extraData": {
            "xtToken": "Vf7W9vvndb4dyrNm8Ms9292Oc+dPG+Mmhj1hPnyIj1Y=",
            "weiboEnable": false
        },
        "accessToken": "IIfQVw5mx2izGMmr7NZMNA3Rg3qzvk6V4huxOQRQDFdwRRE6UFXkWcksoxeuzpGQfRiZKZfV155UI3KSIvsMc/ljKWpuMy0A"
    },
    "error": "0",
    "errorCode": 0,
    "isIjf": "true",
    "name": "张玲",
    "personId": "10019046",
    "sessionId": "35627967353436795648566c494546315a7941774f4341784e6a6f7a4e446f304e694244553151674d6a41784e773d3d",
    "success": true
}
 * @author qiph
 * @version 1.0
 */
public class EasOriResponse {
	protected String _cid;
	protected String _key;
	protected String code;
	protected EasOriResponseData data;
	protected String error;
	protected int errorCode;
	protected String isIjf;
	protected String name;
	protected String personId;
	protected String sessionId;
	protected boolean success;
	
	public class EasOriResponseData {
		protected EasOriResponseExtraData extraData;
		protected String accessToken;
		public EasOriResponseExtraData getExtraData() {
			return extraData;
		}
		public void setExtraData(EasOriResponseExtraData extraData) {
			this.extraData = extraData;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
	}
	
	public class EasOriResponseExtraData {
		protected String xtToken;
		protected boolean weiboEnable;
		public String getXtToken() {
			return xtToken;
		}
		public void setXtToken(String xtToken) {
			this.xtToken = xtToken;
		}
		public boolean isWeiboEnable() {
			return weiboEnable;
		}
		public void setWeiboEnable(boolean weiboEnable) {
			this.weiboEnable = weiboEnable;
		}
	}

	public String get_cid() {
		return _cid;
	}

	public void set_cid(String _cid) {
		this._cid = _cid;
	}

	public String get_key() {
		return _key;
	}

	public void set_key(String _key) {
		this._key = _key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public EasOriResponseData getData() {
		return data;
	}

	public void setData(EasOriResponseData data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getIsIjf() {
		return isIjf;
	}

	public void setIsIjf(String isIjf) {
		this.isIjf = isIjf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
