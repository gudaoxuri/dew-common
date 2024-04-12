package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.ScriptHelper;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        Assertions.assertEquals("hi", s1.execute("fun1", String.class, "hi"));

        ScriptHelper s2 = $.script(ScriptHelper.ScriptKind.JS,
                "function fun2(D){\r\n" + " var data = JSON.parse(D);\n" + " return $.field.getGenderByIdCard(data.idcard);\n" + "}");
        Assertions.assertEquals("M", s2.execute("fun2", String.class, "{\"idcard\":\"110101201604016117\"}"));

        Assertions.assertEquals(10240, (long) $.eval(ScriptHelper.ScriptKind.JS, Long.class, "1024*10"));
    }

    /**
     * Test eval.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testEval() throws IOException {
        Context context = Context.newBuilder().allowAllAccess(true).build();
        context.eval(Source.create("js", "function fun1(param){return param;}"));
        context.eval(Source.newBuilder("js", "function fun2(param){return param;}", "src.js").build());
        context.eval(Source.create("js", "function fun3(param){return param;}"));
        var result = context.getBindings("js").getMember("fun1").execute("aa").as(String.class);
        Assertions.assertEquals("aa", result);
        result = context.getBindings("js").getMember("fun2").execute("aaa").as(String.class);
        Assertions.assertEquals("aaa", result);
        result = context.getBindings("js").getMember("fun3").execute("aaa").as(String.class);
        Assertions.assertEquals("aaa", result);
    }

}
