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

package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTGeneralSecurityException;
import com.ecfront.dew.common.exception.RTReflectiveOperationException;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Java Bean操作.
 *
 * @author gudaoxuri
 */
public class BeanHelper {

    private NullAwareBeanUtilsBean copyPropertiesAdapter;

    private boolean useCache = true;
    private static final Map<String, Map<String, Field>> FIELDS = new WeakHashMap<>();
    private static final Map<String, List<Method>> METHODS = new WeakHashMap<>();

    /**
     * Instantiates a new Bean helper.
     */
    BeanHelper() {
        if (DependencyHelper.hasDependency("org.apache.commons.beanutils.BeanUtilsBean")) {
            copyPropertiesAdapter = new NullAwareBeanUtilsBean();
        }
    }

    /**
     * Instantiates a new Bean helper.
     *
     * @param useCache 是否启用缓存，启用后会缓存获取过的字段和方法列表
     */
    BeanHelper(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     * Java Bean Copy.
     *
     * @param dest 目标Bean
     * @param ori  源Bean
     */
    public void copyProperties(Object dest, Object ori) throws RTReflectiveOperationException {
        try {
            copyPropertiesAdapter.copyProperties(dest, ori);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RTReflectiveOperationException(e);
        }
    }

    /**
     * Java Bean Copy.
     *
     * @param <T>       the type parameter
     * @param ori       源Bean
     * @param destClazz 目标Bean类型
     * @return Copy Result
     */
    public <T> T copyProperties(Object ori, Class<T> destClazz) throws RTGeneralSecurityException {
        try {
            T dest = destClazz.newInstance();
            copyProperties(dest, ori);
            return dest;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RTReflectiveOperationException(e);
        }
    }

    /**
     * 获取Class的注解信息.
     *
     * @param <T>             the type parameter
     * @param clazz           目标类
     * @param annotationClass 目标注解
     * @return 注解信息 class annotation
     */
    public <T extends Annotation> T getClassAnnotation(Class<?> clazz, Class<T> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    /**
     * 获取Class的字段信息.
     *
     * @param clazz                    目标Class类型
     * @param excludeNames             要排除的名称，默认为空
     * @param excludeAnnotationClasses 要排除的注解，默认为 Ignore
     * @param includeNames             要包含的名称，默认为全部
     * @param includeAnnotationClasses 要包含的注解，默认为全部
     * @return 字段信息 map
     */
    public Map<String, FieldInfo> findFieldsInfo(Class<?> clazz,
                                                 Set<String> excludeNames, Set<Class<? extends Annotation>> excludeAnnotationClasses,
                                                 Set<String> includeNames, Set<Class<? extends Annotation>> includeAnnotationClasses) {
        final Map<String, FieldInfo> fieldsInfo = new HashMap<>();
        if (excludeNames == null) {
            excludeNames = new HashSet<>();
        }
        if (excludeAnnotationClasses == null) {
            excludeAnnotationClasses = new HashSet<>();
        }
        if (includeNames == null) {
            includeNames = new HashSet<>();
        }
        if (includeAnnotationClasses == null) {
            includeAnnotationClasses = new HashSet<>();
        }
        Set<String> finalExcludeNames = excludeNames;
        Set<Class<? extends Annotation>> finalExcludeAnnotationClasses = excludeAnnotationClasses;
        Set<String> finalIncludeNames = includeNames;
        Set<Class<? extends Annotation>> finalIncludeAnnotationClasses = includeAnnotationClasses;
        getFields(clazz).values().stream()
                .filter(f -> !finalExcludeNames.contains(f.getName()))
                .filter(f -> finalExcludeAnnotationClasses.stream()
                        .noneMatch(ann ->
                                Arrays.stream(f.getAnnotations())
                                        .map(Annotation::annotationType)
                                        .collect(Collectors.toSet())
                                        .contains(ann)
                        ))
                .filter(f -> finalIncludeNames.isEmpty() || finalIncludeNames.contains(f.getName()))
                .filter(f -> finalIncludeAnnotationClasses.isEmpty()
                        || finalIncludeAnnotationClasses.stream()
                        .anyMatch(ann ->
                                Arrays.stream(f.getAnnotations())
                                        .map(Annotation::annotationType)
                                        .collect(Collectors.toSet())
                                        .contains(ann)
                        ))
                .forEach(field -> {
                    FieldInfo info = new FieldInfo();
                    info.setName(field.getName());
                    info.setType(field.getType());
                    info.setAnnotations(new HashSet<>(Arrays.asList(field.getAnnotations())));
                    info.setField(field);
                    fieldsInfo.put(field.getName(), info);
                });
        return fieldsInfo;
    }

    /**
     * 获取当前类及父类的所有字段.
     *
     * @param clazz 当前类
     * @return 字段信息 fields
     */
    public Map<String, Field> getFields(Class<?> clazz) {
        Map<String, Field> fields = FIELDS.getOrDefault(clazz.getName(), new HashMap<>());
        if (!fields.isEmpty()) {
            return fields;
        }
        for (Field field : clazz.getDeclaredFields()) {
            fields.put(field.getName(), field);
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            fields.putAll(getFields(clazz.getSuperclass()));
        }
        if (useCache) {
            FIELDS.put(clazz.getName(), fields);
        }
        return fields;
    }

    /**
     * 获取Class的方法信息.
     *
     * @param clazz                    目标Class类型
     * @param excludeNames             要排除的名称，默认为空
     * @param excludeAnnotationClasses 要排除的注解，默认为 Ignore
     * @param includeNames             要包含的名称，默认为全部
     * @param includeAnnotationClasses 要包含的注解，默认为全部
     * @return 方法信息 list
     */
    public List<MethodInfo> findMethodsInfo(Class<?> clazz,
                                            Set<String> excludeNames, Set<Class<? extends Annotation>> excludeAnnotationClasses,
                                            Set<String> includeNames, Set<Class<? extends Annotation>> includeAnnotationClasses) {
        final List<MethodInfo> methodsInfo = new ArrayList<>();
        if (excludeNames == null) {
            excludeNames = new HashSet<>();
        }
        if (excludeAnnotationClasses == null) {
            excludeAnnotationClasses = new HashSet<>();
        }
        if (includeNames == null) {
            includeNames = new HashSet<>();
        }
        if (includeAnnotationClasses == null) {
            includeAnnotationClasses = new HashSet<>();
        }
        Set<String> finalExcludeNames = excludeNames;
        Set<Class<? extends Annotation>> finalExcludeAnnotationClasses = excludeAnnotationClasses;
        Set<String> finalIncludeNames = includeNames;
        Set<Class<? extends Annotation>> finalIncludeAnnotationClasses = includeAnnotationClasses;
        getMethods(clazz).stream()
                .filter(m -> !finalExcludeNames.contains(m.getName()))
                .filter(m -> finalExcludeAnnotationClasses.stream()
                        .noneMatch(ann ->
                                Arrays.stream(m.getAnnotations())
                                        .map(Annotation::annotationType)
                                        .collect(Collectors.toSet())
                                        .contains(ann)
                        ))
                .filter(m -> finalIncludeNames.isEmpty() || finalIncludeNames.contains(m.getName()))
                .filter(m -> finalIncludeAnnotationClasses.isEmpty()
                        || finalIncludeAnnotationClasses.stream()
                        .anyMatch(ann ->
                                Arrays.stream(m.getAnnotations())
                                        .map(Annotation::annotationType)
                                        .collect(Collectors.toSet())
                                        .contains(ann)
                        ))
                .forEach(method -> {
                    MethodInfo info = new MethodInfo();
                    info.setName(method.getName());
                    info.setReturnType(method.getReturnType());
                    info.setAnnotations(new HashSet<>(Arrays.asList(method.getAnnotations())));
                    info.setMethod(method);
                    methodsInfo.add(info);
                });
        return methodsInfo;
    }

    /**
     * 获取当前类及父类的所有方法.
     *
     * @param clazz 当前类
     * @return 方法信息 methods
     */
    public List<Method> getMethods(Class<?> clazz) {
        List<Method> methods = METHODS.getOrDefault(clazz.getName(), new ArrayList<>());
        if (!methods.isEmpty()) {
            return methods;
        }
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            methods.addAll(getMethods(clazz.getSuperclass()));
        }
        if (useCache) {
            METHODS.put(clazz.getName(), methods);
        }
        return methods;
    }

    /**
     * 获取字段对应的Get/Set方法.
     *
     * @param clazz                    目标Class类型
     * @param excludeNames             要排除的名称，默认为空
     * @param excludeAnnotationClasses 要排除的注解，默认为 Ignore
     * @param includeNames             要包含的名称，默认为全部
     * @param includeAnnotationClasses 要包含的注解，默认为全部
     * @return 字段对应的Get /Set方法
     */
    public Map<String, Method[]> parseRelFieldAndMethod(Class<?> clazz, Set<String> excludeNames,
                                                        Set<Class<? extends Annotation>> excludeAnnotationClasses,
                                                        Set<String> includeNames, Set<Class<? extends Annotation>> includeAnnotationClasses) throws
            RTReflectiveOperationException {
        Map<String, Method[]> rel = new HashMap<>();
        Map<String, FieldInfo> fieldsInfo = findFieldsInfo(clazz, excludeNames, excludeAnnotationClasses, includeNames,
                includeAnnotationClasses);
        try {
            for (Map.Entry<String, FieldInfo> info : fieldsInfo.entrySet()) {
                rel.put(info.getKey(), new Method[]{
                        clazz.getMethod(packageMethodNameByField(info.getValue().getField(), false)),
                        clazz.getMethod(packageMethodNameByField(info.getValue().getField(), true), info.getValue().getType())});
            }
        } catch (NoSuchMethodException e) {
            throw new RTReflectiveOperationException(e);
        }
        return rel;
    }

    /**
     * 根据字段的Get方法获取对应的值.
     *
     * @param obj               目标对象
     * @param relFieldAndMethod 字段对应的Get/Set方法
     * @return 值列表 map
     */
    public Map<String, Object> findValuesByRel(Object obj, Map<String, Method[]> relFieldAndMethod) {
        Map<String, Object> values = new HashMap<>();
        for (Map.Entry<String, Method[]> rel : relFieldAndMethod.entrySet()) {
            values.put(rel.getKey(), getValue(obj, rel.getValue()[0]));
        }
        return values;
    }

    /**
     * 根据字段信息获取对应的值.
     *
     * @param obj        目标对象
     * @param fieldsInfo 目标字段
     * @return 值列表 map
     */
    public Map<String, Object> findValues(Object obj, Map<String, FieldInfo> fieldsInfo) {
        Map<String, Object> values = new HashMap<>();
        for (Map.Entry<String, FieldInfo> info : fieldsInfo.entrySet()) {
            values.put(info.getKey(), getValue(obj, info.getValue().getField()));
        }
        return values;
    }

    /**
     * 获取对象所有字段的值.
     *
     * @param obj                      目标对象
     * @param excludeNames             要排除的名称，默认为空
     * @param excludeAnnotationClasses 要排除的注解，默认为 Ignore
     * @param includeNames             要包含的名称，默认为全部
     * @param includeAnnotationClasses 要包含的注解，默认为全部
     * @return 值列表 map
     */
    public Map<String, Object> findValues(Object obj, Set<String> excludeNames, Set<Class<? extends
            Annotation>> excludeAnnotationClasses,
                                          Set<String> includeNames, Set<Class<? extends Annotation>> includeAnnotationClasses) {
        Map<String, Object> values = new HashMap<>();
        findFieldsInfo(obj.getClass(), excludeNames, excludeAnnotationClasses, includeNames, includeAnnotationClasses)
                .forEach((k, v) -> values.put(k, getValue(obj, v.getField())));
        return values;
    }

    /**
     * 根据方法获取对应的值.
     *
     * @param obj    目标对象
     * @param method 目标方法
     * @return 对应的值 value
     */
    public Object getValue(Object obj, Method method) throws RTReflectiveOperationException {
        return invoke(obj, method);
    }

    /**
     * 根据字段名称获取对应的值.
     *
     * @param obj       目标对象
     * @param fieldName 目标字段名称
     * @return 对应的值 value
     */
    public Object getValue(Object obj, String fieldName) throws RTReflectiveOperationException {
        Map<String, Field> fields = getFields(obj.getClass());
        if (fields.containsKey(fieldName)) {
            return getValue(obj, fields.get(fieldName));
        } else {
            throw new RTReflectiveOperationException();
        }
    }

    /**
     * 根据字段获取对应的值.
     *
     * @param obj   目标对象
     * @param field 目标字段
     * @return 对应的值 value
     */
    public Object getValue(Object obj, Field field) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据方法设置值.
     *
     * @param obj    目标对象
     * @param method 目标方法
     * @param value  要设置的值
     */
    public void setValue(Object obj, Method method, Object value) throws RTReflectiveOperationException {
        invoke(obj, method, value);
    }

    /**
     * 根据字段名称设置值.
     *
     * @param obj       目标对象
     * @param fieldName 目标字段名称
     * @param value     要设置的值
     */
    public void setValue(Object obj, String fieldName, Object value) throws RTReflectiveOperationException {
        Map<String, Field> fields = getFields(obj.getClass());
        if (fields.containsKey(fieldName)) {
            setValue(obj, fields.get(fieldName), value);
        } else {
            throw new RTReflectiveOperationException();
        }
    }

    /**
     * 根据字段设置值.
     *
     * @param obj   目标对象
     * @param field 目标字段
     * @param value 要设置的值
     */
    public void setValue(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行方法.
     *
     * @param obj    目标对象
     * @param method 目标方法
     * @param args   参数
     * @return 执行结果 object
     */
    public Object invoke(Object obj, Method method, Object... args) throws RTReflectiveOperationException {
        method.setAccessible(true);
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RTReflectiveOperationException(e);
        }
    }

    private String packageMethodNameByField(Field field, boolean isSet) {
        if (isSet) {
            if (field.getType() == boolean.class && field.getName().startsWith("is") && field.getName().length() > 3) {
                Character c = field.getName().substring(2, 1).toCharArray()[0];
                if (c >= 'A' && c <= 'Z') {
                    return "set" + field.getName().substring(2);
                }
            }
            return "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        } else {
            if (field.getType() == boolean.class) {
                if (field.getName().startsWith("is") && field.getName().length() > 3) {
                    Character c = field.getName().substring(2, 1).toCharArray()[0];
                    if (c >= 'A' && c <= 'Z') {
                        return field.getName();
                    }
                }
                return "is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            }
            return "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        }
    }

    /**
     * The type Null aware bean utils bean.
     */
    static class NullAwareBeanUtilsBean extends BeanUtilsBean {
        @Override
        public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
            if (null != value) {
                super.copyProperty(bean, name, value);
            }
        }
    }

    /**
     * 反射的字段（属性）信息.
     */
    public static class FieldInfo {

        /**
         * 字段名.
         */
        private String name;
        /**
         * 字段类型.
         */
        private Class<?> type;
        /**
         * 注解列表.
         */
        private Set<Annotation> annotations;
        /**
         * Field.
         */
        private Field field;

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets type.
         *
         * @return the type
         */
        public Class<?> getType() {
            return type;
        }

        /**
         * Sets type.
         *
         * @param type the type
         */
        public void setType(Class<?> type) {
            this.type = type;
        }

        /**
         * Gets annotations.
         *
         * @return the annotations
         */
        public Set<Annotation> getAnnotations() {
            return annotations;
        }

        /**
         * Sets annotations.
         *
         * @param annotations the annotations
         */
        public void setAnnotations(Set<Annotation> annotations) {
            this.annotations = annotations;
        }

        /**
         * Gets field.
         *
         * @return the field
         */
        public Field getField() {
            return field;
        }

        /**
         * Sets field.
         *
         * @param field the field
         */
        public void setField(Field field) {
            this.field = field;
        }
    }

    /**
     * 反射的方法信息.
     *
     * @author gudaoxuri
     */
    public static class MethodInfo {

        /**
         * 方法名.
         */
        private String name;
        /**
         * 返回类型.
         */
        private Class<?> returnType;
        /**
         * 注解列表.
         */
        private Set<Annotation> annotations;
        /**
         * Method.
         */
        private Method method;

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets return type.
         *
         * @return the return type
         */
        public Class<?> getReturnType() {
            return returnType;
        }

        /**
         * Sets return type.
         *
         * @param returnType the return type
         */
        public void setReturnType(Class<?> returnType) {
            this.returnType = returnType;
        }

        /**
         * Gets annotations.
         *
         * @return the annotations
         */
        public Set<Annotation> getAnnotations() {
            return annotations;
        }

        /**
         * Sets annotations.
         *
         * @param annotations the annotations
         */
        public void setAnnotations(Set<Annotation> annotations) {
            this.annotations = annotations;
        }

        /**
         * Gets method.
         *
         * @return the method
         */
        public Method getMethod() {
            return method;
        }

        /**
         * Sets method.
         *
         * @param method the method
         */
        public void setMethod(Method method) {
            this.method = method;
        }
    }

}
