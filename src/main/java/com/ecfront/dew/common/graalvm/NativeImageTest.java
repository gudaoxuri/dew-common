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

package com.ecfront.dew.common.graalvm;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.ScriptHelper;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Native image test.
 *
 * @author gudaoxuri
 */
public class NativeImageTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = manager.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            System.out.println(">>" + factory);
        }

        check("零元整".equals($.amount.convert("0.00")));

        Set<Class<?>> resultInFile = $.clazz.scan("com.ecfront.dew.common.test", new HashSet<>() {
            {
                add(Deprecated.class);
            }
        }, null);
        check(0 == resultInFile.size());

        check($.field.validateEmail("i@sunisle.org"));

        String result = $.http.get("https://httpbin.org/get");
        check("httpbin.org".equals($.json.toJson(result).get("headers").get("Host").asText()));

        check(
                "{\"\":{\"a_key\":\"a_val\"}}".equals(
                        $.json.toJsonString($.json.createObjectNode().set("", $.json.createObjectNode().put("a_key", "a_val")))));

        check("gudaoxuri".equals($.security.symmetric.decrypt(
                $.security.symmetric.encrypt("gudaoxuri", "pwd", "aes"), "pwd", "aes")));


        ScriptHelper s1 = $.script(ScriptHelper.ScriptKind.JS, "function fun1(param){return param;}");
        check("hi".equals(s1.execute("fun1", "hi")));

    }

    private static void check(boolean result) {
        if (!result) {
            throw new RuntimeException("check( fail");
        }
    }
}
