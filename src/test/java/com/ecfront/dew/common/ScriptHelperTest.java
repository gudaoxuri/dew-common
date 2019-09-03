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
 * The type Script helper test.
 *
 * @author gudaoxuri
 */
public class ScriptHelperTest {

    /**
     * Test script.
     *
     * @throws Exception the exception
     */
    @Test
    public void testScript() throws Exception {
        ScriptHelper s1 = $.script("function fun1(param){return param;}");
        Assert.assertEquals("hi", s1.execute("fun1", "hi"));

        ScriptHelper s2 = $.script("function fun2(D){\r\n"
                + " var data = JSON.parse(D);\n"
                + " return $.field.getGenderByIdCard(data.idcard);\n"
                + "}");
        Assert.assertEquals("M", s2.execute("fun2", "{\"idcard\":\"110101201604016117\"}"));

        Assert.assertEquals(10240, $.eval("1024*10"));

    }

}
