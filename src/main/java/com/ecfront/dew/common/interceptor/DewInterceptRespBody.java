package com.ecfront.dew.common.interceptor;

import java.util.Map;

public class DewInterceptRespBody<E> {

    private E obj;
    private Map<String, Object> context;

    public static <E> DewInterceptRespBody<E> build(E obj, Map<String, Object> context) {
        DewInterceptRespBody<E> body = new DewInterceptRespBody<>();
        body.setObj(obj);
        body.setContext(context);
        return body;
    }

    public E getObj() {
        return obj;
    }

    public void setObj(E obj) {
        this.obj = obj;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

}
