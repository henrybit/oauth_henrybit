package com.jf.oauth.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 字符串处理工具类
 * Created by ffw team on 2016/11/28.
 * @version 1.0
 */
public class StringTools {

    protected static final char[] words = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
    protected static final char[] numbers = {'0','1','2','3','4','5','6','7','8','9'};

    /**
     * 判断一个字符串是否为空
     * @param str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (str == null || str == "")
            return true;
        return false;
    }

    public static boolean isArrayEmpty(String[] array) {
        if (array == null || array.length <= 0)
            return true;
        return false;
    }

    /**
     * 判断一个字符串是否非空
     * @param str
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 返回处理过的字符串(处理掉空串)
     * @param str
     * @return String
     */
    public static String getString(String str) {
        if (str == null || str == "")
            return "";
        return str;
    }

    public static String[] split(String str, String delimit) {
        if (str == null || str == "")
            return null;
        try {
            String[] array = str.split(delimit);
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * long转string
     * @param value
     * @return
     */
    public static String longToString(Long value) {
        if (value == null) return "";
        return value+"";
    }

    /**
     * 随机一定长度的字符串
     * @param length
     * @return String
     */
    public static String random(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<length; i++) {
            int p = random.nextInt(words.length);
            sb.append(words[p]);
        }
        return sb.toString();
    }

    /**
     * 返回一个6位长的随机码
     * @return String
     */
    public static String randomCode() {
        return randomCode(6);
    }

    /**
     * 返回一个指定长度的随机码
     * @param length
     * @return String
     */
    public static String randomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i=0; i<length; i++) {
            int p = random.nextInt(numbers.length);
            code.append(numbers[p]);
        }
        return code.toString();
    }

    public static void main(String[] args) {
        List<Long> ids = new ArrayList<Long>();
        ids.add(5L);

        int v = 5;
        System.out.println(ids.contains(new Long(v)));

        String a = null;
        a = "fdafda";
        System.out.println(isEmpty(a));
    }
}
