package com.jf.oauth.auth.entity;

import com.jf.oauth.core.entity.ResponseConstant;
import com.jf.oauth.core.tools.JSONTools;

/**
 * 基本响应报文
 * @author qiph
 * @version 1.0
 */
public class BaseResponse {
	//返回状态码
	protected int code;
	//返回信息
	protected String msg;
	//返回数据
	protected Object data;
	
	/**
	 * 创建一个成功的响应报文
	 * @param data 
	 * @return BaseResponse
	 */
	public static BaseResponse createSuccess(Object data) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.code = ResponseConstant.SUCCESS;
		baseResponse.data = data;
		return baseResponse;
	}
	
	/**
	 * 创建一个失败的响应报文
	 * @param code
	 * @param msg
	 * @return BaseResponse
	 */
	public static BaseResponse createFailed(int code, String msg) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.code = code;
		baseResponse.msg = msg;
		return baseResponse;
	}
	
	/**
	 * 获取JSON格式
	 * @return
	 */
	public String body() {
		return JSONTools.toJson(this);
	}
}
