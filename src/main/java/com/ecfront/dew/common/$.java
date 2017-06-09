package com.ecfront.dew.common;

/**
 * DEW Common 操作入口
 */
public class $ {

    /**
     * Json与Java对象互转
     */
    public static JsonHelper json = new JsonHelper();

    /**
     * Java Bean操作
     */
    public static BeanHelper bean = new BeanHelper();

    /**
     * Java Class扫描操作
     */
    public static ClassScanHelper clazz = new ClassScanHelper();

    /**
     * 加解密操作
     */
    public static EncryptHelper encrypt = new EncryptHelper();

    /**
     * 常用字段操作
     */
    public static FieldHelper field = new FieldHelper();

    /**
     * 常用文件操作
     */
    public static FileHelper file = new FileHelper();

    /**
     * MIME信息操作
     */
    public static MimeHelper mime = new MimeHelper();

    /**
     * 时间操作
     */
    public static TimeHelper time = new TimeHelper();

    /**
     * 定时器操作
     */
    public static TimerHelper timer = new TimerHelper();

    /**
     * Shell脚本操作
     */
    public static ShellHelper shell() {
        return new ShellHelper();
    }

    /**
     * 拦截器栈执行器
     */
    public static InterceptorHelper interceptor = new InterceptorHelper();

    /**
     * Http操作
     */
    public static HttpHelper http = new HttpHelper();

    /**
     * Http操作
     *
     * @param maxTotal    整个连接池最大连接数
     * @param maxPerRoute 每个路由（域）的默认最大连接
     */
    public static HttpHelper http(int maxTotal, int maxPerRoute) {
        return new HttpHelper(maxTotal, maxPerRoute);
    }

    /**
     * 金额操作
     */
    public static AmountHelper amount = new AmountHelper();

}
