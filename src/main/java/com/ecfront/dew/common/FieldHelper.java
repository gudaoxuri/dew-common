package com.ecfront.dew.common;

import com.ecfront.dew.common.inner.IdcardUtils;

import java.util.regex.Pattern;

/**
 * 常用字段操作
 */
public class FieldHelper {

    private static final Pattern EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]*[A-Z0-9]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern MOBILE_REGEX =
            Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    private static final Pattern CHINESE_REGEX =
            Pattern.compile("^[\u4e00-\u9fa5]+$");


    FieldHelper(){}

    /**
     * 验证邮箱格式是否合法
     *
     * @param email 邮件
     */
    public boolean validateEmail(String email) {
        return EMAIL_ADDRESS_REGEX.matcher(email).find();
    }

    /**
     * 验证手机号格式是否合法
     *
     * @param mobile 手机号
     */
    public boolean validateMobile(String mobile) {
        return MOBILE_REGEX.matcher(mobile).find();
    }

    /**
     * 验证身份证是否合法
     *
     * @param idNumber 身份编号
     */
    public boolean validateIdNumber(String idNumber) {
        return IdcardUtils.validateCard(idNumber);
    }

    /**
     * 是否是汉字
     *
     * @param str 字符串
     */
    public boolean isChinese(String str) {
        return CHINESE_REGEX.matcher(str).find();
    }

    /**
     * 根据身份编号获取年龄
     *
     * @param idNumber 身份编号
     * @return 年龄
     */
    public int getAgeByIdCard(String idNumber) {
        return IdcardUtils.getAgeByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idNumber 身份编号
     * @return 生日(yyyyMMdd)
     */
    public String getBirthByIdCard(String idNumber) {
        return IdcardUtils.getBirthByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idNumber 身份编号
     * @return 生日(yyyy)
     */
    public Short getYearByIdCard(String idNumber) {
        return IdcardUtils.getYearByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idNumber 身份编号
     * @return 生日(MM)
     */
    public Short getMonthByIdCard(String idNumber) {
        return IdcardUtils.getMonthByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idNumber 身份编号
     * @return 生日(dd)
     */
    public Short getDateByIdCard(String idNumber) {
        return IdcardUtils.getDateByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idNumber 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public String getGenderByIdCard(String idNumber) {
        return IdcardUtils.getGenderByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param idNumber 身份编码
     * @return 省级编码。
     */
    public String getProvinceByIdCard(String idNumber) {
        return IdcardUtils.getProvinceByIdCard(idNumber);
    }


}
