package com.jf.oauth.core.tools;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * JSON工具类<br>
 * Created by ffw team on 2016/11/29.
 * @version 1.0
 */
public class JSONTools {
    private final static Gson gson = new Gson();

    /**
     * 指定对象转换成JSON数据<br>
     * Obj To json
     * @param obj
     * @return String
     */
    public static String toJson(Object obj) {
        if (obj == null)
            return "";
        return gson.toJson(obj);
    }

    /**
     * JSON转换成指定的对象<br>
     * json To T
     * @param json
     * @param cls
     * @param <T>
     * @return T
     */
    public static <T> T parserJson(String json, Class<T> cls) {
        if (StringTools.isEmpty(json))
            return null;
        return gson.fromJson(json, cls);
    }

    /**
     * JSON转成指定对象
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     */
    public static <T> T parserJson(String json, Type typeOfT) {
        if (StringTools.isEmpty(json))
            return null;
        return gson.fromJson(json, typeOfT);
    }
    
    
    public static void main(String[] args) {
    	String json = "{\"_cid\":\"a4b5a28a-cc71-4138-b760-a42ff1e08c52\",\"_key\":\"2d01d532-fc3e-4dca-8789-02b38aab0783\",\"code\":null,\"data\":{\"extraData\":{\"xtToken\":\"Vf7W9vvndb4dyrNm8Ms9292Oc+dPG+Mmhj1hPnyIj1Y=\",\"weiboEnable\":false},\"accessToken\":\"IIfQVw5mx2izGMmr7NZMNA3Rg3qzvk6V4huxOQRQDFdwRRE6UFXkWcksoxeuzpGQfRiZKZfV155UI3KSIvsMc\\\\/ljKWpuMy0A\"},\"error\":\"0\",\"errorCode\":0,\"isIjf\":\"true\",\"name\":\"张玲\",\"personId\":\"10019046\",\"sessionId\":\"35627967353436795648566c494546315a7941774f4341784e6a6f7a4e446f304e694244553151674d6a41784e773d3d\",\"success\":true}";
    }
}
