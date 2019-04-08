package com.ecfront.dew.common;

import javax.script.*;

public class ScriptHelper {

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    private Invocable invocable;

    private ScriptHelper(Invocable invocable) {
        this.invocable = invocable;
    }

    public static ScriptHelper build(String jsFunsCode, boolean addCommonCode) throws ScriptException {
        Compilable jsEngine = (Compilable) SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn");
        if (addCommonCode) {
            jsFunsCode = "var $ = Java.type('com.ecfront.dew.common.$');\r\n" + jsFunsCode;
        }
        CompiledScript script = jsEngine.compile(jsFunsCode);
        script.eval();
        return new ScriptHelper((Invocable) script.getEngine());
    }

    public <T> T execute(String jsFunName, Object... args) throws ScriptException, NoSuchMethodException {
        return (T) invocable.invokeFunction(jsFunName, args);
    }

    public static Object eval(String jsCode) throws ScriptException {
        return SCRIPT_ENGINE_MANAGER.getEngineByName("nashorn").eval(jsCode);
    }

}
