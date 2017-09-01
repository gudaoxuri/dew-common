package com.ecfront.dew.common;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间操作
 */
public class TimeHelper {

    TimeHelper(){}

    public final SimpleDateFormat msf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    public final SimpleDateFormat mf = new SimpleDateFormat("yyyyMMddHHmm");
    public final SimpleDateFormat hf = new SimpleDateFormat("yyyyMMddHH");
    public final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    public final SimpleDateFormat Mf = new SimpleDateFormat("yyyyMM");
    public final SimpleDateFormat yf = new SimpleDateFormat("yyyy");

    public final SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public final SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

    public static String utc2Local(String utcTime, String localTimeFormat) {
        Date utcDate = Date.from(ZonedDateTime.parse(utcTime).toInstant());
        SimpleDateFormat localF = new SimpleDateFormat(localTimeFormat);
        localF.setTimeZone(TimeZone.getDefault());
        return localF.format(utcDate.getTime());
    }

}
