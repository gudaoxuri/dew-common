package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The type Field helper test.
 *
 * @author gudaoxuri
 */
public class FieldHelperTest {

    /**
     * Test field.
     */
    @Test
    public void testField() {
        Assertions.assertTrue($.field.validateEmail("i@sunisle.org"));
        Assertions.assertTrue($.field.validateEmail("fy53.org@gmail.com"));
        Assertions.assertFalse($.field.validateEmail("i@sunisle"));
        Assertions.assertFalse($.field.validateEmail("i@sunisle..org"));
        Assertions.assertFalse($.field.validateEmail("@sunisle.org"));
        Assertions.assertFalse($.field.validateEmail("i#sunisle.org"));

        Assertions.assertTrue($.field.validateMobile("14726847687"));
        Assertions.assertTrue($.field.validateMobile("15326847687"));
        Assertions.assertTrue($.field.validateMobile("15526847687"));
        Assertions.assertTrue($.field.validateMobile("18657120000"));
        Assertions.assertTrue($.field.validateMobile("13765712000"));
        Assertions.assertTrue($.field.validateMobile("17714712000"));
        Assertions.assertTrue($.field.validateMobile("16614712000"));
        Assertions.assertTrue($.field.validateMobile("19914712000"));
        Assertions.assertTrue($.field.validateMobile("19814712000"));
        Assertions.assertFalse($.field.validateMobile("1865712000"));
        Assertions.assertFalse($.field.validateMobile("28657120000"));
        Assertions.assertFalse($.field.validateMobile("11657120000"));
        Assertions.assertFalse($.field.validateMobile("15426847687"));
        Assertions.assertFalse($.field.validateMobile("15?26847687"));

        Assertions.assertTrue($.field.isChinese("孤岛旭日"));
        Assertions.assertFalse($.field.isChinese("孤岛xuri"));
        Assertions.assertFalse($.field.isChinese("gudaoxuri"));

        Assertions.assertTrue($.field.isIPv4Address("127.0.0.1"));
        Assertions.assertTrue($.field.isIPv4Address("192.168.0.1"));
        Assertions.assertFalse($.field.isIPv4Address("300.168.0.1"));

        Assertions.assertTrue($.field.validateIdNumber("330102199901015759"));
        Assertions.assertEquals("19990101", $.field.getBirthByIdCard("330102199901015759"));
        Assertions.assertEquals("M", $.field.getGenderByIdCard("330102199901015759"));
        Assertions.assertEquals("浙江", $.field.getProvinceByIdCard("330102199901015759"));
        Assertions.assertEquals(1, (short) $.field.getDateByIdCard("330102199901015759"));
        Assertions.assertEquals(1, (short) $.field.getMonthByIdCard("330102199901015759"));
        Assertions.assertEquals(1999, (short) $.field.getYearByIdCard("330102199901015759"));
    }


}
