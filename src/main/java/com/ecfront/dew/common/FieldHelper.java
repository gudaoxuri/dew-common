/*
 * Copyright 2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecfront.dew.common;

import com.ecfront.dew.common.inner.IdcardUtils;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 常用字段操作.
 *
 * @author gudaoxuri
 */
public class FieldHelper {

    private static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]*[A-Z0-9]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$");

    private static final Pattern CHINESE_PATTERN =
            Pattern.compile("^[\u4e00-\u9fa5]+$");

    private static final Pattern IPV4_PATTERN =
            Pattern.compile(
                    "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN =
            Pattern.compile(
                    "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
            Pattern.compile(
                    "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    FieldHelper() {
    }

    /**
     * 验证邮箱格式是否合法.
     *
     * @param email 邮件
     * @return 邮箱是否合法
     */
    public boolean validateEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式是否合法.
     *
     * @param mobile 手机号
     * @return 手机号是否合法
     */
    public boolean validateMobile(String mobile) {
        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    /**
     * 验证身份证是否合法.
     *
     * @param idNumber 身份编号
     * @return 身份证号验证是否通过
     */
    public boolean validateIdNumber(String idNumber) {
        return IdcardUtils.validateCard(idNumber);
    }


    /**
     * 验证是否是IPv4.
     *
     * @param str 待校验字符串
     * @return 是否是IPv4
     */
    public boolean isIPv4Address(String str) {
        return IPV4_PATTERN.matcher(str).matches();
    }

    /**
     * 验证是否是IPv6.
     *
     * @param str 待校验字符串
     * @return 是否是IPv6
     */
    public boolean isIPv6Address(String str) {
        return isIPv6StdAddress(str) || isIPv6HexCompressedAddress(str);
    }

    private boolean isIPv6StdAddress(String str) {
        return IPV6_STD_PATTERN.matcher(str).matches();
    }

    private boolean isIPv6HexCompressedAddress(String str) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(str).matches();
    }

    /**
     * 是否是汉字.
     *
     * @param str 待校验字符串
     * @return 是否是汉字
     */
    public boolean isChinese(String str) {
        return CHINESE_PATTERN.matcher(str).matches();
    }

    /**
     * 根据身份编号获取年龄.
     *
     * @param idNumber 身份编号
     * @return 年龄
     */
    public int getAgeByIdCard(String idNumber) {
        return IdcardUtils.getAgeByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日.
     *
     * @param idNumber 身份编号
     * @return 生日(yyyyMMdd)
     */
    public String getBirthByIdCard(String idNumber) {
        return IdcardUtils.getBirthByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日年.
     *
     * @param idNumber 身份编号
     * @return 生日(yyyy)
     */
    public Short getYearByIdCard(String idNumber) {
        return IdcardUtils.getYearByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日月.
     *
     * @param idNumber 身份编号
     * @return 生日(MM)
     */
    public Short getMonthByIdCard(String idNumber) {
        return IdcardUtils.getMonthByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日天.
     *
     * @param idNumber 身份编号
     * @return 生日(dd)
     */
    public Short getDateByIdCard(String idNumber) {
        return IdcardUtils.getDateByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取性别.
     *
     * @param idNumber 身份编号
     * @return 性别(M - 男 ， F - 女 ， N - 未知)
     */
    public String getGenderByIdCard(String idNumber) {
        return IdcardUtils.getGenderByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取户籍省份.
     *
     * @param idNumber 身份编码
     * @return 省级编码
     */
    public String getProvinceByIdCard(String idNumber) {
        return IdcardUtils.getProvinceByIdCard(idNumber);
    }


    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 获取UUID.
     *
     * @return UUID
     */
    public String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取短UUID.
     *
     * @return 短UUID
     */
    public String createShortUUID() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = createUUID();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

}
