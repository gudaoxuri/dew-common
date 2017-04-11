package com.ecfront.dew.common.interceptor;

import com.ecfront.dew.common.Resp;

import java.util.Map;

@FunctionalInterface
public interface DewInterceptExec<E> {

    Resp<DewInterceptRespBody<E>> exec(E obj, Map<String, Object> context);

}
