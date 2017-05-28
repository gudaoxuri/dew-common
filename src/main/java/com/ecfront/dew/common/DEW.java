package com.ecfront.dew.common;

import com.ecfront.dew.common.interceptor.DewInterceptorProcessor;

/**
 * DEW Common 操作入口
 */
public class DEW {

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
    public static ShellProcessor shell() {
        return new ShellProcessor();
    }

    /**
     * 拦截器栈执行器
     */
    public static DewInterceptorProcessor interceptor = new DewInterceptorProcessor();
}
