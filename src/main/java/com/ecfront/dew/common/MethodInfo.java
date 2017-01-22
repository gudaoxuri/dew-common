package com.ecfront.dew.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class MethodInfo {

    private String name;
    private Class<?> returnType;
    private Set<Annotation> annotations;
    private Method method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
