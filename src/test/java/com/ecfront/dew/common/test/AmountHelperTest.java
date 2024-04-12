package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The type Amount helper test.
 *
 * @author gudaoxuri
 */
public class AmountHelperTest {

    /**
     * Test convert.
     */
    @Test
    public void testConvert() {
        Assertions.assertEquals("零元整", $.amount.convert("0.00"));
        Assertions.assertEquals("壹仟元整", $.amount.convert("1000"));
        Assertions.assertEquals("壹仟元整", $.amount.convert("1000.0"));
        Assertions.assertEquals("壹仟元壹角", $.amount.convert("1000.1"));
        Assertions.assertEquals("壹仟元整", $.amount.convert("1000.00"));

        Assertions.assertEquals("壹万陆仟肆佰零玖元零贰分", $.amount.convert("16,409.02"));
        Assertions.assertEquals("壹仟肆佰零玖元伍角", $.amount.convert("1,409.50"));
        Assertions.assertEquals("陆仟零柒元壹角肆分", $.amount.convert("6,007.14"));
        Assertions.assertEquals("壹仟陆佰捌拾元叁角贰分", $.amount.convert("1,680.32"));
        Assertions.assertEquals("叁佰贰拾伍元零肆分", $.amount.convert("325.04"));
        Assertions.assertEquals("肆仟叁佰贰拾壹元整", $.amount.convert("4,321.00"));
        Assertions.assertEquals("壹分", $.amount.convert("0.01"));

        Assertions.assertEquals("壹仟贰佰叁拾肆亿伍仟陆佰柒拾捌万玖仟零壹拾贰元叁角肆分", $.amount.convert("1234,5678,9012.34"));
        Assertions.assertEquals("壹仟亿零壹仟万零壹仟元壹角", $.amount.convert("1000,1000,1000.10"));
        Assertions.assertEquals("玖仟零玖亿玖仟零玖万玖仟零玖元玖角玖分", $.amount.convert("9009,9009,9009.99"));
        Assertions.assertEquals("伍仟肆佰叁拾贰亿零壹万零壹元零壹分", $.amount.convert("5432,0001,0001.01"));
        Assertions.assertEquals("壹仟亿零壹仟壹佰壹拾元整", $.amount.convert("1000,0000,1110.00"));
        Assertions.assertEquals("壹仟零壹拾亿零壹元壹角壹分", $.amount.convert("1010,0000,0001.11"));
        Assertions.assertEquals("壹仟亿元零壹分", $.amount.convert("1000,0000,0000.01"));
    }

}
