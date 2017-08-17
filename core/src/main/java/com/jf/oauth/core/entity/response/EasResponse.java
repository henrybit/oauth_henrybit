package com.jf.oauth.core.entity.response;

import com.jf.oauth.core.tools.JSONTools;

/**
 * 金蝶相关相应报文<br>
 * @author qiph
 * @version 1.0
 */
public class EasResponse {
	protected int code;
	protected Object data;
	protected String errMsg;
	
	public String getBody() {
		return JSONTools.toJson(this);
	}
	
	public String buildJSONMessage() {
		return JSONTools.toJson(this);
	}
	/**
	 * 创建返回报文
	 * @param code 返回状态码（参考org.apache.http.HttpStatus）
	 * @param data 返回数据包
	 * @param errMsg 返回异常信息
	 * @return EasResponse
	 */
	public static EasResponse createResponse(int code, Object data, String errMsg) {
		EasResponse easResponse = new EasResponse();
		easResponse.code = code;
		easResponse.data = data;
		easResponse.errMsg = errMsg;
		return easResponse;
	}
}
