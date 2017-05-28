package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

public class FieldHelperTest {

    @Test
    public void testField() throws Exception {
        Assert.assertTrue(DEW.field.validateEmail("i@sunisle.org"));
        Assert.assertTrue(DEW.field.validateEmail("fy53.org@gmail.com"));
        Assert.assertFalse(DEW.field.validateEmail("i@sunisle"));
        Assert.assertFalse(DEW.field.validateEmail("i@sunisle..org"));
        Assert.assertFalse(DEW.field.validateEmail("@sunisle.org"));
        Assert.assertFalse(DEW.field.validateEmail("i#sunisle.org"));

        Assert.assertTrue(DEW.field.validateMobile("18657120000"));
        Assert.assertTrue(DEW.field.validateMobile("13765712000"));
        Assert.assertTrue(DEW.field.validateMobile("17714712000"));
        Assert.assertFalse(DEW.field.validateMobile("1865712000"));
        Assert.assertFalse(DEW.field.validateMobile("28657120000"));
        Assert.assertFalse(DEW.field.validateMobile("11657120000"));

        Assert.assertTrue(DEW.field.isChinese("孤岛旭日"));
        Assert.assertFalse(DEW.field.isChinese("孤岛xuri"));
        Assert.assertFalse(DEW.field.isChinese("gudaoxuri"));

        Assert.assertTrue(DEW.field.validateIdNumber("330102199901015759"));
        Assert.assertEquals(18, DEW.field.getAgeByIdCard("330102199901015759"));
        Assert.assertEquals("19990101", DEW.field.getBirthByIdCard("330102199901015759"));
        Assert.assertEquals("M", DEW.field.getGenderByIdCard("330102199901015759"));
        Assert.assertEquals("浙江", DEW.field.getProvinceByIdCard("330102199901015759"));
        Assert.assertTrue(1 == DEW.field.getDateByIdCard("330102199901015759"));
        Assert.assertTrue(1 == DEW.field.getMonthByIdCard("330102199901015759"));
        Assert.assertTrue(1999 == DEW.field.getYearByIdCard("330102199901015759"));
    }


}