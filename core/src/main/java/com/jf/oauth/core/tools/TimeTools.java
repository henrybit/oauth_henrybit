package com.jf.oauth.core.tools;

import java.util.Calendar;
import java.util.Date;

import com.jf.oauth.core.entity.OauthConstant;

/**
 * 时间工具类<br>
 * Created by henrybit on 2017/5/5.
 * @version 1.0
 */
public class TimeTools {

    /**
     * 获取当前系统时间
     * @return Date
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前时间+指定分钟后的时间
     * @param minutes 增加的分钟数
     * @return Date-增加后的时间
     */
    public static Date addCurrentDateByMinutes(int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
    /**
     * 获取当前时间+指定秒后的时间
     * @param seconds 增加的秒数
     * @return Date-增加后的时间
     */
    public static Date addCurrentDateBySeconds(int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static void main(String[] args) {
        System.out.println(addCurrentDateBySeconds(OauthConstant.AUTH_CODE_EXPIRE_TIME));
    }
}
