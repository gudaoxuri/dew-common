package com.ecfront.dew.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

public class FieldInfo {

    private String name;
    private Class<?> type;
    private Set<Annotation> annotations;
    private Field field;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
