package com.ecfront.dew.common;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间操作.
 *
 * @author gudaoxuri
 */
public class TimeHelper {

    /**
     * Instantiates a new Time helper.
     */
    TimeHelper() {
    }

    /**
     * The Msf.
     */
    public final SimpleDateFormat msf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    /**
     * The Sf.
     */
    public final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * The Mf.
     */
    public final SimpleDateFormat mf = new SimpleDateFormat("yyyyMMddHHmm");
    /**
     * The Hf.
     */
    public final SimpleDateFormat hf = new SimpleDateFormat("yyyyMMddHH");
    /**
     * The Df.
     */
    public final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    /**
     * The Mf.
     */
    public final SimpleDateFormat Mf = new SimpleDateFormat("yyyyMM");
    /**
     * The Yf.
     */
    public final SimpleDateFormat yf = new SimpleDateFormat("yyyy");

    /**
     * The Yyyy mm dd hh mm ss sss.
     */
    public final SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    /**
     * The Yyyy mm dd hh mm ss.
     */
    public final SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * The Yyyy mm dd.
     */
    public final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Utc 2 local.
     *
     * @param utcTime         the utc time
     * @param localTimeFormat the local time format
     * @return the string
     */
    public static String utc2Local(String utcTime, String localTimeFormat) {
        Date utcDate = Date.from(ZonedDateTime.parse(utcTime).toInstant());
        SimpleDateFormat localF = new SimpleDateFormat(localTimeFormat);
        localF.setTimeZone(TimeZone.getDefault());
        return localF.format(utcDate.getTime());
    }

}
