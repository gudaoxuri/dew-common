package com.ecfront.dew.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 金额操作.
 *
 * @author Based By https://gist.github.com/binjoo/6028263
 */
public class AmountHelper {

    private static final Pattern AMOUNT_PATTERN =
            Pattern.compile("^(0|[1-9]\\d{0,11})\\.(\\d\\d)$"); // 不考虑分隔符的正确性
    private static final char[] RMB_NUMS = "零壹贰叁肆伍陆柒捌玖".toCharArray();
    private static final String[] UNITS = {"元", "角", "分", "整"};
    private static final String[] U1 = {"", "拾", "佰", "仟"};
    private static final String[] U2 = {"", "万", "亿"};

    /**
     * 将金额（整数部分等于或少于12位，小数部分2位）转换为中文大写形式.
     *
     * @param amount 金额数字
     * @return 中文大写 string
     * @throws IllegalArgumentException the illegal argument exception
     */
    public String convert(String amount) throws IllegalArgumentException {
        amount = amount.replace(",", "");
        String[] amountArray = amount.split("\\.");
        if (amountArray.length != 2) {
            amount = amount + ".00";
        } else {
            if (amountArray[1].length() == 1) {
                amount = amount + "0";
            }
        }
        if (amount.equals("0.00")) {
            return "零元整";
        }
        Matcher matcher = AMOUNT_PATTERN.matcher(amount);
        if (!matcher.find()) {
            throw new IllegalArgumentException("输入金额有误.");
        }
        String integer = matcher.group(1); // 整数部分
        String fraction = matcher.group(2); // 小数部分
        StringBuilder sb = new StringBuilder("");
        if (!integer.equals("0")) {
            sb.append(integer2rmb(integer)).append(UNITS[0]); // 整数部分
        }
        if (fraction.equals("00")) {
            sb.append(UNITS[3]); // 添加[整]
        } else if (fraction.startsWith("0") && integer.equals("0")) {
            sb.append(fraction2rmb(fraction).substring(1)); // 去掉分前面的[零]
        } else {
            sb.append(fraction2rmb(fraction)); // 小数部分
        }
        return sb.toString();
    }

    // 将金额小数部分转换为中文大写
    private static String fraction2rmb(String fraction) {
        char jiao = fraction.charAt(0); // 角
        char fen = fraction.charAt(1); // 分
        return (RMB_NUMS[jiao - '0'] + (jiao > '0' ? UNITS[1] : ""))
                + (fen > '0' ? RMB_NUMS[fen - '0'] + UNITS[2] : "");
    }

    // 将金额整数部分转换为中文大写
    private static String integer2rmb(String integer) {
        StringBuilder buffer = new StringBuilder();
        // 从个位数开始转换
        int i;
        int j;
        for (i = integer.length() - 1, j = 0; i >= 0; i--, j++) {
            char n = integer.charAt(i);
            if (n == '0') {
                // 当n是0且n的右边一位不是0时，插入[零]
                if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
                    buffer.append(RMB_NUMS[0]);
                }
                // 插入[万]或者[亿]
                if (j % 4 == 0) {
                    if (i > 0 && integer.charAt(i - 1) != '0'
                            || i > 1 && integer.charAt(i - 2) != '0'
                            || i > 2 && integer.charAt(i - 3) != '0') {
                        buffer.append(U2[j / 4]);
                    }
                }
            } else {
                if (j % 4 == 0) {
                    buffer.append(U2[j / 4]);     // 插入[万]或者[亿]
                }
                buffer.append(U1[j % 4]);         // 插入[拾]、[佰]或[仟]
                buffer.append(RMB_NUMS[n - '0']); // 插入数字
            }
        }
        return buffer.reverse().toString();
    }

}
