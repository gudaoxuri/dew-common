/*
 * Copyright 2019. the original author or authors.
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
 * The interface Dew intercept exec.
 *
 * @param <I> the type parameter
 * @param <O> the type parameter
 * @author gudaoxuri
 */
@FunctionalInterface
public interface DewInterceptExec<I, O> {

    /**
     * Exec resp.
     *
     * @param context the context
     * @return the resp
     */
    Resp<DewInterceptContext<I, O>> exec(DewInterceptContext<I, O> context);

}
