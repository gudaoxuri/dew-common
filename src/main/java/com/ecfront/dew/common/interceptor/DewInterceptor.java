package com.ecfront.dew.common.interceptor;

import com.ecfront.dew.common.Resp;

/**
 * 拦截器栈定义.
 *
 * @param <I> 输入对象的类型
 * @param <O> 输出对象的类型
 * @author gudaoxuri
 */
public interface DewInterceptor<I, O> {

    /**
     * 获取拦截器所属类型，用于区别不同的栈.
     *
     * @return the category
     */
    String getCategory();

    /**
     * 获取拦截器名称.
     *
     * @return the name
     */
    String getName();

    /**
     * 前置执行.
     *
     * @param context 操作上下文
     * @return 执行后结果 resp
     */
    Resp<DewInterceptContext<I, O>> before(DewInterceptContext<I, O> context);

    /**
     * 后置执行.
     *
     * @param context 操作上下文
     * @return 执行后结果 resp
     */
    Resp<DewInterceptContext<I, O>> after(DewInterceptContext<I, O> context);

    /**
     * 错误处理，在前置/后置执行错误时触发，多用于资源回收.
     *
     * @param context 操作上下文
     */
    default void error(DewInterceptContext<I, O> context) {
        // Do Nothing.
    }

}
