package com.ecfront.dew.common.interceptor;

import com.ecfront.dew.common.Resp;

import java.util.Map;

/**
 * 拦截器栈定义
 *
 * @param <E> 对象的类型
 */
public interface DewInterceptor<E> {

    /**
     * 获取拦截器所属类型，用于区别不同的栈
     */
    String getCategory();

    /**
     * 获取拦截器名称
     */
    String getName();

    /**
     * 前置执行
     *
     * @param obj     对象
     * @param context 参数
     * @return 执行后结果
     */
    Resp<DewInterceptRespBody<E>> before(E obj, Map<String, Object> context);

    /**
     * 后置执行
     *
     * @param obj     对象
     * @param context 参数
     * @return 执行后结果
     */
    Resp<DewInterceptRespBody<E>> after(E obj, Map<String, Object> context);

    /**
     * 错误处理，在前置/后置执行错误时触发，多用于资源回收
     *
     * @param obj     对象
     * @param context 参数
     */
    default void error(E obj, Map<String, Object> context) {

    }

}
