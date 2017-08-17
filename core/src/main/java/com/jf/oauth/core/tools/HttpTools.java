package com.jf.oauth.core.tools;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Http请求工具<br>
 * @author qiph
 * @version 1.0
 */
public class HttpTools {
	
	/**
	 * POST方式请求URL
	 * @param url
	 * @param parameters 请求参数（除了String,Integer,Long,Double,FLoat会转为字符串，Boolean会变成0和1，其他都会自动转成JSON格式）
	 * @return String-返回报文
	 */
	public static String post(String url, Map<String, Object> parameters) {
		String responsebody = "";
		// 创建默认的httpClient实例.    
	    CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if (parameters!=null && parameters.size()>0) {
        	Iterator<String> iterator = parameters.keySet().iterator();
        	while(iterator.hasNext()) {
        		String key = iterator.next();
        		Object value = parameters.get(key);
        		if (value == null) continue;
        		if (value instanceof String) {
        			String rvalue = (String)value;
        			formparams.add(new BasicNameValuePair(key, rvalue));
        		} else if (value instanceof Integer ) {
        			String rvalue = (Integer)value+"";
        			formparams.add(new BasicNameValuePair(key, rvalue));
        		} else if (value instanceof Long) {
        			String rvalue = (Long)value+"";
        			formparams.add(new BasicNameValuePair(key, rvalue));
        		} else if (value instanceof Float) {
        			String rvalue = (Float)value+"";
        			formparams.add(new BasicNameValuePair(key, rvalue));
        		} else if (value instanceof Double) {
        			String rvalue = (Double)value+"";
        			formparams.add(new BasicNameValuePair(key, rvalue));
        		} else if (value instanceof Boolean){
        			Boolean rvalue = (Boolean)value;
        			formparams.add(new BasicNameValuePair(key, rvalue?"1":"0"));
        		} else {
        			String rvalue = JSONTools.toJson(value);
        			formparams.add(new BasicNameValuePair(key, rvalue));
        		}
        	}
        }
        UrlEncodedFormEntity uefEntity;  
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                HttpEntity entity = response.getEntity();  
                if (entity != null) {
                	responsebody = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("--------------------------------------");  
                    System.out.println("Response content: " + responsebody);  
                    System.out.println("--------------------------------------");  
                }  
            } finally {  
                response.close();  
            }  
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
        return responsebody;
	}
	
	/**
	 * Ge请求
	 * @param oriUrl 根地址
	 * @param parameters 请求参数（除了String,Integer,Long,Double,FLoat会转为字符串，Boolean会变成0和1，其他都会自动转成JSON格式）
	 * @return String
	 */
	public static String get(String oriUrl, Map<String, Object> parameters) {
		String responsebody = "";
		if (StringTools.isEmpty(oriUrl))
			return responsebody;
		CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {
        	boolean isFirstParam = true;
        	if (oriUrl.indexOf("?") >= 0)
        		isFirstParam = false;
        	StringBuilder url = new StringBuilder(oriUrl);
        	if (parameters!=null && parameters.size()>0) {
            	Iterator<String> iterator = parameters.keySet().iterator();
            	while(iterator.hasNext()) {
            		String key = iterator.next();
            		Object value = parameters.get(key);
            		if (value == null) continue;
            		if (value instanceof String) {
            			String rvalue = (String)value;
            			if (isFirstParam)
            				url.append("?").append(key).append("=").append(rvalue);
            			else
            				url.append("&").append(key).append("=").append(rvalue);
            			isFirstParam = false;
            		} else if (value instanceof Integer ) {
            			String rvalue = (Integer)value+"";
            			if (isFirstParam)
            				url.append("?").append(key).append("=").append(rvalue);
            			else
            				url.append("&").append(key).append("=").append(rvalue);
            			isFirstParam = false;
            		} else if (value instanceof Long) {
            			String rvalue = (Long)value+"";
            			if (isFirstParam)
            				url.append("?").append(key).append("=").append(rvalue);
            			else
            				url.append("&").append(key).append("=").append(rvalue);
            			isFirstParam = false;
            		} else if (value instanceof Float) {
            			String rvalue = (Float)value+"";
            			if (isFirstParam)
            				url.append("?").append(key).append("=").append(rvalue);
            			else
            				url.append("&").append(key).append("=").append(rvalue);
            			isFirstParam = false;
            		} else if (value instanceof Double) {
            			String rvalue = (Double)value+"";
            			if (isFirstParam)
            				url.append("?").append(key).append("=").append(rvalue);
            			else
            				url.append("&").append(key).append("=").append(rvalue);
            			isFirstParam = false;
            		} else if (value instanceof Boolean){
            			Boolean rvalue = (Boolean)value;
            			if (isFirstParam)
            				url.append("?").append(key).append("=").append(rvalue);
            			else
            				url.append("&").append(key).append("=").append(rvalue);
            			isFirstParam = false;
            		} else {
            			String rvalue = JSONTools.toJson(value);
            			rvalue = URLDecoder.decode(rvalue, "utf-8"); //url编码
            			if (isFirstParam)
            				url.append("?").append(key).append("=").append(rvalue);
            			else            		
            				url.append("&").append(key).append("=").append(rvalue);
            			isFirstParam = false;
            		}
            	}
            }
            //创建httpget
            HttpGet httpget = new HttpGet(url.toString());
            System.out.println("executing request " + httpget.getURI());  
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
                System.out.println("--------------------------------------");  
                // 打印响应状态    
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                	responsebody = EntityUtils.toString(entity, "UTF-8");
                    // 打印响应内容长度    
                    System.out.println("Response content length: " + entity.getContentLength());  
                    // 打印响应内容    
                    System.out.println("Response content: " + responsebody);  
                }  
                System.out.println("------------------------------------");  
            } finally {  
                response.close();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
        return responsebody;
	}
}
