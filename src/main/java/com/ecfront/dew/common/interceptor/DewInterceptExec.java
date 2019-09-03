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
