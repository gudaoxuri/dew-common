package com.ecfront.dew.common.inner;

import com.ecfront.dew.common.exception.RTException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证工具类.
 *
 * @author Based By June  http://www.oschina.net/code/snippet_1611_2881
 */
public final class IdcardUtils {

    private IdcardUtils() {
    }

    /**
     * 中国公民身份证号码最小长度.
     */
    private static final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度.
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 每位加权因子.
     */
    private static final int[] POWER = {
            7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
    };

    /**
     * 最低年限.
     */
    private static final int MIN = 1930;
    private static final Map<String, String> CITY_CODES = new HashMap<>();

    static {
        CITY_CODES.put("11", "北京");
        CITY_CODES.put("12", "天津");
        CITY_CODES.put("13", "河北");
        CITY_CODES.put("14", "山西");
        CITY_CODES.put("15", "内蒙古");
        CITY_CODES.put("21", "辽宁");
        CITY_CODES.put("22", "吉林");
        CITY_CODES.put("23", "黑龙江");
        CITY_CODES.put("31", "上海");
        CITY_CODES.put("32", "江苏");
        CITY_CODES.put("33", "浙江");
        CITY_CODES.put("34", "安徽");
        CITY_CODES.put("35", "福建");
        CITY_CODES.put("36", "江西");
        CITY_CODES.put("37", "山东");
        CITY_CODES.put("41", "河南");
        CITY_CODES.put("42", "湖北");
        CITY_CODES.put("43", "湖南");
        CITY_CODES.put("44", "广东");
        CITY_CODES.put("45", "广西");
        CITY_CODES.put("46", "海南");
        CITY_CODES.put("50", "重庆");
        CITY_CODES.put("51", "四川");
        CITY_CODES.put("52", "贵州");
        CITY_CODES.put("53", "云南");
        CITY_CODES.put("54", "西藏");
        CITY_CODES.put("61", "陕西");
        CITY_CODES.put("62", "甘肃");
        CITY_CODES.put("63", "青海");
        CITY_CODES.put("64", "宁夏");
        CITY_CODES.put("65", "新疆");
        CITY_CODES.put("71", "台湾");
        CITY_CODES.put("81", "香港");
        CITY_CODES.put("82", "澳门");
        CITY_CODES.put("91", "国外");
    }

    /**
     * 将15位身份证号码转换为18位.
     *
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    private static String convert15CardTo18(String idCard) {
        String idCard18;
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (isNum(idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(6, 12);
            Date birthDate;
            try {
                birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                throw new RTException(e);
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            // 转换字符数组
            char[] cArr = idCard18.toCharArray();
            int[] iCard = convertCharToInt(cArr);
            int iSum17 = getPowerSum(iCard);
            // 获取校验位
            String sVal = getCheckCode18(iSum17);
            if (sVal.length() > 0) {
                idCard18 += sVal;
            } else {
                return null;
            }
        } else {
            return null;
        }
        return idCard18;
    }

    /**
     * 验证身份证是否合法.
     *
     * @param idCard 身份证号码
     * @return 验证是否通过
     */
    public static boolean validateCard(String idCard) {
        String card = idCard.trim();
        return validateIdCard18(card) || validateIdCard15(card);
    }

    /**
     * 验证18位身份编码是否合法.
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    private static boolean validateIdCard18(String idCard) {
        boolean bTrue = false;
        if (idCard.length() == CHINA_ID_MAX_LENGTH) {
            // 前17位
            String code17 = idCard.substring(0, 17);
            // 第18位
            String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
            if (isNum(code17)) {
                char[] cArr = code17.toCharArray();
                int[] iCard = convertCharToInt(cArr);
                int iSum17 = getPowerSum(iCard);
                // 获取校验位
                String val = getCheckCode18(iSum17);
                if (val.length() > 0) {
                    if (val.equalsIgnoreCase(code18)) {
                        bTrue = true;
                    }
                }
            }
        }
        return bTrue;
    }

    /**
     * 验证15位身份编码是否合法.
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    private static boolean validateIdCard15(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return false;
        }
        if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (CITY_CODES.get(proCode) == null) {
                return false;
            }
            String birthCode = idCard.substring(6, 12);
            Date birthDate;
            try {
                birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
            } catch (ParseException e) {
                throw new RTException(e);
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            return validateDate(cal.get(Calendar.YEAR), Integer.parseInt(birthCode.substring(2, 4)),
                    Integer.parseInt(birthCode.substring(4, 6)));
        } else {
            return false;
        }
    }

    /**
     * 将字符数组转换成数字数组.
     *
     * @param ca 字符数组
     * @return 数字数组
     */
    private static int[] convertCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            throw new RTException(e);
        }
        return iArr;
    }

    private static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < POWER.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * POWER[j];
                    }
                }
            }
        }
        return iSum;
    }

    private static String getCheckCode18(int iSum) {
        String sCode;
        switch (iSum % 11) {
            case 10:
                sCode = "2";
                break;
            case 9:
                sCode = "3";
                break;
            case 8:
                sCode = "4";
                break;
            case 7:
                sCode = "5";
                break;
            case 6:
                sCode = "6";
                break;
            case 5:
                sCode = "7";
                break;
            case 4:
                sCode = "8";
                break;
            case 3:
                sCode = "9";
                break;
            case 2:
                sCode = "x";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
            default:
                throw new RTException();
        }
        return sCode;
    }

    /**
     * 根据身份编号获取年龄.
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = convert15CardTo18(idCard);
        }
        String year = idCard.substring(6, 10);
        Calendar cal = Calendar.getInstance();
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.parseInt(year);
        return iAge;
    }

    /**
     * 根据身份编号获取生日.
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15CardTo18(idCard);
        }
        return idCard.substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年.
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月.
     *
     * @param idCard 身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天.
     *
     * @param idCard 身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
        int len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = convert15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(12, 14));
    }

    /**
     * 根据身份编号获取性别.
     *
     * @param idCard 身份编号
     * @return 性别(M - 男 ， F - 女 ， N - 未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = convert15CardTo18(idCard);
        }
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "M";
        } else {
            sGender = "F";
        }
        return sGender;
    }

    /**
     * 根据身份编号获取户籍省份.
     *
     * @param idCard 身份编码
     * @return 省级编码。
     */
    public static String getProvinceByIdCard(String idCard) {
        int len = idCard.length();
        String sProvince;
        String sProvinNum = "";
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            sProvinNum = idCard.substring(0, 2);
        }
        sProvince = CITY_CODES.get(sProvinNum);
        return sProvince;
    }

    private static boolean isNum(String val) {
        return !(val == null || "".equals(val)) && val.matches("^[0-9]*$");
    }

    /**
     * 验证小于当前日期 是否有效.
     *
     * @param iYear  待验证日期(年)
     * @param iMonth 待验证日期(月 1-12)
     * @param iDate  待验证日期(日)
     * @return 是否有效
     */
    private static boolean validateDate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int datePerMonth;
        if (iYear < MIN || iYear >= year) {
            return false;
        }
        if (iMonth < 1 || iMonth > 12) {
            return false;
        }
        switch (iMonth) {
            case 4:
            case 6:
            case 9:
            case 11:
                datePerMonth = 30;
                break;
            case 2:
                boolean dm = (iYear % 4 == 0 && iYear % 100 != 0 || iYear % 400 == 0) && iYear > MIN;
                datePerMonth = dm ? 29 : 28;
                break;
            default:
                datePerMonth = 31;
        }
        return (iDate >= 1) && (iDate <= datePerMonth);
    }
}
