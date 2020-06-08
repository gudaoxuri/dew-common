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

package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTScriptException;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;

import java.io.IOException;
import java.util.Arrays;

/**
 * The type Script helper.
 *
 * @author gudaoxuri
 */
public class ScriptHelper {

    /**
     * The enum Script kind.
     */
    public enum ScriptKind {
        /**
         * Js script kind.
         */
        JS("js"),
        /**
         * Python script kind.
         */
        PYTHON("python"),
        /**
         * Ruby script kind.
         */
        RUBY("ruby"),
        /**
         * R script kind.
         */
        R("r");

        private final String code;

        ScriptKind(String code) {
            this.code = code;
        }

        /**
         * Parse script kind.
         *
         * @param code the code
         * @return the script kind
         */
        public static ScriptKind parse(String code) {
            return Arrays.stream(ScriptKind.values())
                    .filter(item -> item.code.equalsIgnoreCase(code))
                    .findFirst()
                    .orElseThrow(() -> new RTScriptException("Script kind {" + code + "} NOT exist."));
        }

        @Override
        public String toString() {
            return code;
        }
    }

    private Context context;
    private ScriptKind scriptKind;

    private ScriptHelper(Context context, ScriptKind scriptKind) {
        this.context = context;
        this.scriptKind = scriptKind;
    }

    /**
     * Build script helper.
     *
     * @param scriptKind     the script kind
     * @param scriptFunsCode the script funs code
     * @param addCommonCode  the add common code
     * @return the script helper
     * @throws RTScriptException the rt script exception
     */
    public static ScriptHelper build(ScriptKind scriptKind, String scriptFunsCode, boolean addCommonCode) throws RTScriptException {
        try {
            Context context = Context.newBuilder().build();
            if (addCommonCode) {
                switch (scriptKind) {
                    case JS:
                        scriptFunsCode = "var $ = Java.type('com.ecfront.dew.common.$')\r\n" + scriptFunsCode;
                        break;
                    case PYTHON:
                        // TODO
                        scriptFunsCode = "import java\r\n$ = java.type(\"com.ecfront.dew.common.$\")\r\n" + scriptFunsCode;
                        break;
                    case RUBY:
                        // TODO
                        scriptFunsCode = "$ = Java.type('com.ecfront.dew.common.$')\r\n" + scriptFunsCode;
                        break;
                    case R:
                        // TODO
                        scriptFunsCode = "$ <- java.type('com.ecfront.dew.common.$')\r\n" + scriptFunsCode;
                        break;
                    default:
                        throw new RTScriptException("Script kind {" + scriptKind.toString() + "} NOT exist.");
                }
            }
            context.eval(Source.newBuilder(scriptKind.toString(), scriptFunsCode, "src.js").build());
            return new ScriptHelper(context, scriptKind);
        } catch (IOException e) {
            throw new RTScriptException(e);
        }
    }

    /**
     * Execute.
     *
     * @param <T>     the type parameter
     * @param funName the fun name
     * @param args    the args
     * @return the t
     */
    public <T> T execute(String funName, Object... args) {
        return (T) context.getBindings(scriptKind.toString()).getMember(funName).execute(args);
    }

    /**
     * Eval object.
     *
     * @param scriptKind the script kind
     * @param scriptCode the script code
     * @return the object
     * @throws RTScriptException the rt script exception
     */
    public static Object eval(ScriptKind scriptKind, String scriptCode) {
        try (Context context = Context.create()) {
            return context.eval(scriptKind.toString(), scriptCode);
        }
    }

}
