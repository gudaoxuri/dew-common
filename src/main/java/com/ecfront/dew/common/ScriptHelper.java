package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTReflectiveOperationException;
import com.ecfront.dew.common.exception.RTScriptException;

import javax.script.*;

/**
 * The type Script helper.
 *
 * @author gudaoxuri
 */
public class ScriptHelper {

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    private Invocable invocable;

    private ScriptHelper(Invocable invocable) {
        this.invocable = invocable;
    }

    /**
     * Build script helper.
     *
     * @param jsFunsCode    the js funs code
     * @param addCommonCode the add common code
     * @return the script helper
     * @throws RTScriptException the rt script exception
     */
    public static ScriptHelper build(String jsFunsCode, boolean addCommonCode) throws RTScriptException {
        Compilable jsEngine = (Compilable) SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");
        if (addCommonCode) {
            jsFunsCode = "var $ = Java.type('com.ecfront.dew.common.$');\r\n" + jsFunsCode;
        }
        try {
            CompiledScript script = jsEngine.compile(jsFunsCode);
            script.eval();
            return new ScriptHelper((Invocable) script.getEngine());
        } catch (ScriptException e) {
            throw new RTScriptException(e);
        }
    }

    /**
     * Execute.
     *
     * @param <T>       the type parameter
     * @param jsFunName the js fun name
     * @param args      the args
     * @return the t
     * @throws RTScriptException              the rt script exception
     * @throws RTReflectiveOperationException the rt reflective operation exception
     */
    public <T> T execute(String jsFunName, Object... args) throws RTScriptException, RTReflectiveOperationException {
        try {
            return (T) invocable.invokeFunction(jsFunName, args);
        } catch (ScriptException e) {
            throw new RTScriptException(e);
        } catch (NoSuchMethodException e) {
            throw new RTReflectiveOperationException(e);
        }
    }

    /**
     * Eval object.
     *
     * @param jsCode the js code
     * @return the object
     * @throws RTScriptException the rt script exception
     */
    public static Object eval(String jsCode) throws RTScriptException {
        try {
            return SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn").eval(jsCode);
        } catch (ScriptException e) {
            throw new RTScriptException(e);
        }
    }

}
