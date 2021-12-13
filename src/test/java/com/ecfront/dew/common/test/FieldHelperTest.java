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
