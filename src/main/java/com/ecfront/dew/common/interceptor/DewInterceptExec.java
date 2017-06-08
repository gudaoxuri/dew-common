package com.ecfront.dew.common.interceptor;

import com.ecfront.dew.common.Resp;

@FunctionalInterface
public interface DewInterceptExec<I, O> {

    Resp<DewInterceptContext<I, O>> exec(DewInterceptContext<I, O> context);

}
