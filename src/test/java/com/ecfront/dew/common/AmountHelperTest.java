/*
 * Copyright 2019. the original author or authors.
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

import org.junit.Assert;
import org.junit.Test;

/**
 * The type Amount helper test.
 *
 * @author gudaoxuri
 */
public class AmountHelperTest {

    /**
     * Test convert.
     *
     * @throws Exception the exception
     */
    @Test
    public void testConvert() throws Exception {
        Assert.assertEquals("零元整", $.amount.convert("0.00"));
        Assert.assertEquals("壹仟元整", $.amount.convert("1000"));
        Assert.assertEquals("壹仟元整", $.amount.convert("1000.0"));
        Assert.assertEquals("壹仟元壹角", $.amount.convert("1000.1"));
        Assert.assertEquals("壹仟元整", $.amount.convert("1000.00"));

        Assert.assertEquals("壹万陆仟肆佰零玖元零贰分", $.amount.convert("16,409.02"));
        Assert.assertEquals("壹仟肆佰零玖元伍角", $.amount.convert("1,409.50"));
        Assert.assertEquals("陆仟零柒元壹角肆分", $.amount.convert("6,007.14"));
        Assert.assertEquals("壹仟陆佰捌拾元叁角贰分", $.amount.convert("1,680.32"));
        Assert.assertEquals("叁佰贰拾伍元零肆分", $.amount.convert("325.04"));
        Assert.assertEquals("肆仟叁佰贰拾壹元整", $.amount.convert("4,321.00"));
        Assert.assertEquals("壹分", $.amount.convert("0.01"));

        Assert.assertEquals("壹仟贰佰叁拾肆亿伍仟陆佰柒拾捌万玖仟零壹拾贰元叁角肆分", $.amount.convert("1234,5678,9012.34"));
        Assert.assertEquals("壹仟亿零壹仟万零壹仟元壹角", $.amount.convert("1000,1000,1000.10"));
        Assert.assertEquals("玖仟零玖亿玖仟零玖万玖仟零玖元玖角玖分", $.amount.convert("9009,9009,9009.99"));
        Assert.assertEquals("伍仟肆佰叁拾贰亿零壹万零壹元零壹分", $.amount.convert("5432,0001,0001.01"));
        Assert.assertEquals("壹仟亿零壹仟壹佰壹拾元整", $.amount.convert("1000,0000,1110.00"));
        Assert.assertEquals("壹仟零壹拾亿零壹元壹角壹分", $.amount.convert("1010,0000,0001.11"));
        Assert.assertEquals("壹仟亿元零壹分", $.amount.convert("1000,0000,0000.01"));
    }

}
