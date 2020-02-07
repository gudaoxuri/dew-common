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
