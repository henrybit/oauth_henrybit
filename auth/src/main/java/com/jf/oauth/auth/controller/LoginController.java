package com.jf.oauth.auth.controller;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jf.oauth.auth.service.ApiService;
import com.jf.oauth.core.entity.ApiToken;
import com.jf.oauth.core.entity.response.EasResponse;
import com.jf.oauth.core.tools.NumberTools;
import com.jf.oauth.core.tools.SecurityApiTools;
import com.jf.oauth.core.tools.StringTools;

/**
 * 登陆Controller<br>
 * @author qiph
 * @version 1.0
 */
@RestController
@RequestMapping(value="/login")
public class LoginController {
	
	@Autowired
	private ApiService apiService;
	
	@RequestMapping(value="eas.json")
	public Object easLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("text","html",Charset.forName("utf-8"));
        headers.setContentType(mediaType);
        
		String code = request.getParameter("code");
		if (StringTools.isEmpty(code)) {
			EasResponse easResponse = EasResponse.createResponse(org.apache.http.HttpStatus.SC_OK, null, "无效的签名");
			return new ResponseEntity(easResponse.getBody(), headers, HttpStatus.valueOf(org.apache.http.HttpStatus.SC_OK));
		}
		System.out.println("code="+code);
		//时间戳+";"+客户端ID+";"+登陆账号
		String decrptCode = SecurityApiTools.decodeEas(code);
		long time = 0;
		String clientId = "";
		String userName = "";
		if (StringTools.isNotEmpty(decrptCode)) {
			String[] array = decrptCode.split(";");
			if (array!=null && array.length==3) {
				time = NumberTools.parseLong(array[0]);
				clientId = array[1];
				userName = array[2];
			}
		}
		if (time<=0 || StringTools.isEmpty(clientId) || StringTools.isEmpty(userName)) {
			EasResponse easResponse = EasResponse.createResponse(org.apache.http.HttpStatus.SC_OK, null, "无效的时间戳或无效的客户端ID或无效的账号");
			return new ResponseEntity(easResponse.getBody(), headers, HttpStatus.valueOf(org.apache.http.HttpStatus.SC_OK));
		}
		
		//时间必须在5分钟内的加密串才是有效的
		long diffTime = Math.abs(System.currentTimeMillis()-time);
		if (diffTime > 300000) { //超过5分钟 
			EasResponse easResponse = EasResponse.createResponse(org.apache.http.HttpStatus.SC_OK, null, "过期的签名");
			return new ResponseEntity(easResponse.getBody(), headers, HttpStatus.valueOf(org.apache.http.HttpStatus.SC_OK));
		}
		//获取登陆token
		ApiToken apiToken = apiService.getToken(clientId, userName);
		EasResponse easResponse = null;
		if (apiToken != null)
			easResponse = EasResponse.createResponse(org.apache.http.HttpStatus.SC_OK, apiToken, null);
		else
			easResponse = EasResponse.createResponse(org.apache.http.HttpStatus.SC_OK, apiToken, "创建登陆Token失败");
		
		return new ResponseEntity(easResponse.getBody(), headers, HttpStatus.valueOf(org.apache.http.HttpStatus.SC_OK));
	}
}
