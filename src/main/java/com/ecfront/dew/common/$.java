package com.ecfront.dew.common;

/**
 * DEW Common 操作入口.
 *
 * @author gudaoxuri
 */
public final class $ {

    private $() {
    }

    /**
     * Json与Java对象互转.
     */
    public static JsonHelper json = JsonHelper.pick("");
    /**
     * Java Bean操作.
     */
    public static BeanHelper bean = new BeanHelper();
    /**
     * Java Class扫描操作.
     */
    public static ClassScanHelper clazz = new ClassScanHelper();
    /**
     * 安全（加解密、信息摘要等）操作.
     */
    public static SecurityHelper security = new SecurityHelper();
    /**
     * 常用字段操作.
     */
    public static FieldHelper field = new FieldHelper();
    /**
     * 常用文件操作.
     */
    public static FileHelper file = new FileHelper();
    /**
     * MIME信息操作.
     */
    public static MimeHelper mime = new MimeHelper();
    /**
     * 定时器操作.
     */
    public static TimerHelper timer = new TimerHelper();
    /**
     * Shell脚本操作.
     */
    public static ShellHelper shell = new ShellHelper();
    /**
     * 拦截器栈执行器.
     */
    public static InterceptorHelper interceptor = new InterceptorHelper();
    /**
     * Http操作.
     */
    public static HttpHelper http = new HttpHelper(-1, true);
    /**
     * 金额操作.
     */
    public static AmountHelper amount = new AmountHelper();
    /**
     * 函数操作.
     */
    public static FunctionHelper fun = new FunctionHelper();
    /**
     * 简单的降级处理.
     */
    public static FallbackHelper fallback = new FallbackHelper();

    /**
     * Json与Java对象互转.
     * <p>
     * 使用自定义实例ID（用于支持不同Json配置）
     *
     * @param instanceId 实例Id
     * @return 对应实例的Json操作 json helper
     */
    public static JsonHelper json(String instanceId) {
        assert instanceId != null && !instanceId.trim().equals("");
        return JsonHelper.pick(instanceId);
    }

    /**
     * Java Bean操作.
     *
     * @param useCache 是否启用缓存，启用后会缓存获取过的字段和方法列表，默认启用
     * @return 是启用缓存的Bean操作 bean helper
     */
    public static BeanHelper bean(boolean useCache) {
        return new BeanHelper(useCache);
    }

    /**
     * 时间操作.
     *
     * @return 时间操作实例 time helper
     */
    public static TimeHelper time() {
        return new TimeHelper();
    }

    /**
     * Http操作.
     *
     * @param timeoutMS    默认超时时间
     * @param autoRedirect 302状态下是否自动跳转
     * @return HTTP操作实例 http helper
     */
    public static HttpHelper http(int timeoutMS, boolean autoRedirect) {
        return new HttpHelper(timeoutMS, autoRedirect);
    }

    /**
     * 脚本处理.
     * <p>
     * 包含预编译，适用于脚本复用性的场景
     *
     * @param scriptKind     the script kind
     * @param scriptFunsCode the script funs code
     * @param addCommonCode  是否添加dew-common包到脚本
     * @return 脚本操作实例 script helper
     */
    public static ScriptHelper script(ScriptHelper.ScriptKind scriptKind, String scriptFunsCode, boolean addCommonCode) {
        return ScriptHelper.build(scriptKind, scriptFunsCode, addCommonCode);
    }

    /**
     * 脚本处理.
     * <p>
     * 包含预编译，适用于脚本复用性的场景，默认添加dew-common包到脚本
     *
     * @param scriptKind     the script kind
     * @param scriptFunsCode the script funs code
     * @return 脚本操作实例 script helper
     */
    public static ScriptHelper script(ScriptHelper.ScriptKind scriptKind, String scriptFunsCode) {
        return ScriptHelper.build(scriptKind, scriptFunsCode, true);
    }

    /**
     * 脚本处理.
     * <p>
     * 适用于简单的脚本的执行
     *
     * @param <T>         the type parameter
     * @param scriptKind  the script kind
     * @param returnClazz the return clazz
     * @param scriptCode  the script code
     * @return 脚本执行结果 object
     */
    public static <T> T eval(ScriptHelper.ScriptKind scriptKind, Class<T> returnClazz, String scriptCode) {
        return ScriptHelper.eval(scriptKind, returnClazz, scriptCode);
    }

}
