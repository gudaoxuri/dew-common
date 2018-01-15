package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

public class FieldHelperTest {

    @Test
    public void testField() throws Exception {
        Assert.assertTrue($.field.validateEmail("i@sunisle.org"));
        Assert.assertTrue($.field.validateEmail("fy53.org@gmail.com"));
        Assert.assertFalse($.field.validateEmail("i@sunisle"));
        Assert.assertFalse($.field.validateEmail("i@sunisle..org"));
        Assert.assertFalse($.field.validateEmail("@sunisle.org"));
        Assert.assertFalse($.field.validateEmail("i#sunisle.org"));

        Assert.assertTrue($.field.validateMobile("14726847687"));
        Assert.assertTrue($.field.validateMobile("15326847687"));
        Assert.assertTrue($.field.validateMobile("15526847687"));
        Assert.assertTrue($.field.validateMobile("18657120000"));
        Assert.assertTrue($.field.validateMobile("13765712000"));
        Assert.assertTrue($.field.validateMobile("17714712000"));
        Assert.assertTrue($.field.validateMobile("16614712000"));
        Assert.assertTrue($.field.validateMobile("19914712000"));
        Assert.assertTrue($.field.validateMobile("19814712000"));
        Assert.assertFalse($.field.validateMobile("1865712000"));
        Assert.assertFalse($.field.validateMobile("28657120000"));
        Assert.assertFalse($.field.validateMobile("11657120000"));
        Assert.assertFalse($.field.validateMobile("15426847687"));
        Assert.assertFalse($.field.validateMobile("15?26847687"));

        Assert.assertTrue($.field.isChinese("孤岛旭日"));
        Assert.assertFalse($.field.isChinese("孤岛xuri"));
        Assert.assertFalse($.field.isChinese("gudaoxuri"));

        Assert.assertTrue($.field.isIPv4Address("127.0.0.1"));
        Assert.assertTrue($.field.isIPv4Address("192.168.0.1"));
        Assert.assertFalse($.field.isIPv4Address("300.168.0.1"));

        Assert.assertTrue($.field.validateIdNumber("330102199901015759"));
        Assert.assertEquals("19990101", $.field.getBirthByIdCard("330102199901015759"));
        Assert.assertEquals("M", $.field.getGenderByIdCard("330102199901015759"));
        Assert.assertEquals("浙江", $.field.getProvinceByIdCard("330102199901015759"));
        Assert.assertTrue(1 == $.field.getDateByIdCard("330102199901015759"));
        Assert.assertTrue(1 == $.field.getMonthByIdCard("330102199901015759"));
        Assert.assertTrue(1999 == $.field.getYearByIdCard("330102199901015759"));
    }


}