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
import com.ecfront.dew.common.ScriptHelper;
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
     */
    @Test
    public void testScript() {
        ScriptHelper s1 = $.script(ScriptHelper.ScriptKind.JS, "function fun1(param){return param;}");
        Assert.assertEquals("hi", s1.execute("fun1", String.class, "hi"));

        ScriptHelper s2 = $.script(ScriptHelper.ScriptKind.JS, "function fun2(D){\r\n"
                + " var data = JSON.parse(D);\n"
                + " return $.field.getGenderByIdCard(data.idcard);\n"
                + "}");
        Assert.assertEquals("M", s2.execute("fun2", String.class, "{\"idcard\":\"110101201604016117\"}"));

        Assert.assertEquals(10240, (long) $.eval(ScriptHelper.ScriptKind.JS, Long.class, "1024*10"));

    }

}
