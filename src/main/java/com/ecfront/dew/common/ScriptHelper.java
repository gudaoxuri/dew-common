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
public final class ScriptHelper {

    private final Context context;
    private final ScriptKind scriptKind;
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
            Context context = Context.newBuilder().allowAllAccess(true).build();
            if (addCommonCode) {
                switch (scriptKind) {
                    case JS:
                        scriptFunsCode = "const $ = Java.type('com.ecfront.dew.common.$')\r\n" + scriptFunsCode;
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
                        throw new RTScriptException("Script kind {" + scriptKind + "} NOT exist.");
                }
            }
            context.eval(Source.newBuilder(scriptKind.toString(), scriptFunsCode, "src.js").build());
            return new ScriptHelper(context, scriptKind);
        } catch (IOException e) {
            throw new RTScriptException(e);
        }
    }

    /**
     * Eval object.
     *
     * @param <T>         the type parameter
     * @param scriptKind  the script kind
     * @param returnClazz the return clazz
     * @param scriptCode  the script code
     * @return the object
     * @throws RTScriptException the rt script exception
     */
    public static <T> T eval(ScriptKind scriptKind, Class<T> returnClazz, String scriptCode) {
        try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
            return context.eval(scriptKind.toString(), scriptCode).as(returnClazz);
        }
    }

    /**
     * Execute.
     *
     * @param <T>         the type parameter
     * @param funName     the fun name
     * @param returnClazz the return clazz
     * @param args        the args
     * @return the t
     */
    public <T> T execute(String funName, Class<T> returnClazz, Object... args) {
        return context.getBindings(scriptKind.toString()).getMember(funName).execute(args).as(returnClazz);
    }

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

}
