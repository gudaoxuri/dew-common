package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

public class FieldHelperTest {

    @Test
    public void testField() throws Exception {
        Assert.assertTrue(FieldHelper.validateEmail("i@sunisle.org"));
        Assert.assertTrue(FieldHelper.validateEmail("fy53.org@gmail.com"));
        Assert.assertFalse(FieldHelper.validateEmail("i@sunisle"));
        Assert.assertFalse(FieldHelper.validateEmail("i@sunisle..org"));
        Assert.assertFalse(FieldHelper.validateEmail("@sunisle.org"));
        Assert.assertFalse(FieldHelper.validateEmail("i#sunisle.org"));

        Assert.assertTrue(FieldHelper.validateMobile("18657120000"));
        Assert.assertTrue(FieldHelper.validateMobile("13765712000"));
        Assert.assertTrue(FieldHelper.validateMobile("17714712000"));
        Assert.assertFalse(FieldHelper.validateMobile("1865712000"));
        Assert.assertFalse(FieldHelper.validateMobile("28657120000"));
        Assert.assertFalse(FieldHelper.validateMobile("11657120000"));

        Assert.assertTrue(FieldHelper.isChinese("孤岛旭日"));
        Assert.assertFalse(FieldHelper.isChinese("孤岛xuri"));
        Assert.assertFalse(FieldHelper.isChinese("gudaoxuri"));

        Assert.assertTrue(FieldHelper.validateIdNumber("330102199901015759"));
        Assert.assertEquals(18, FieldHelper.getAgeByIdCard("330102199901015759"));
        Assert.assertEquals("19990101", FieldHelper.getBirthByIdCard("330102199901015759"));
        Assert.assertEquals("M", FieldHelper.getGenderByIdCard("330102199901015759"));
        Assert.assertEquals("浙江", FieldHelper.getProvinceByIdCard("330102199901015759"));
        Assert.assertTrue(1 == FieldHelper.getDateByIdCard("330102199901015759"));
        Assert.assertTrue(1 == FieldHelper.getMonthByIdCard("330102199901015759"));
        Assert.assertTrue(1999 == FieldHelper.getYearByIdCard("330102199901015759"));
    }


}