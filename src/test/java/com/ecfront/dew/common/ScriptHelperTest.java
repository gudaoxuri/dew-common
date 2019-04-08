package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

public class ScriptHelperTest {

    @Test
    public void testScript() throws Exception {
        ScriptHelper s1 = $.script("function fun1(param){return param;}");
        Assert.assertEquals("hi", s1.execute("fun1", "hi"));

        ScriptHelper s2 = $.script("function fun2(D){\r\n" +
                " var data = JSON.parse(D);\n" +
                " return $.field.getGenderByIdCard(data.idcard);\n" +
                "}");
        Assert.assertEquals("M", s2.execute("fun2", "{\"idcard\":\"110101201604016117\"}"));

    }

}