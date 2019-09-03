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

package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTException;
import com.ecfront.dew.common.exception.RTIOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Json与Java对象互转.
 * <p>
 * 为方便在Java8 Stream中使用，操作返回的异常都是运行时异常
 *
 * @author gudaoxuri
 */
public class JsonHelper {

    private static final Map<String, JsonHelper> INSTANCES = new HashMap<>();

    private ObjectMapper mapper;

    /**
     * Pick json helper.
     *
     * @param instanceId the instance id
     * @return the json helper
     */
    static JsonHelper pick(String instanceId) {
        return INSTANCES.computeIfAbsent(instanceId, k -> new JsonHelper());
    }

    private JsonHelper() {
        if (DependencyHelper.hasDependency("com.fasterxml.jackson.core.JsonProcessingException")) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
            mapper.registerModule(javaTimeModule);
            mapper.registerModule(new Jdk8Module());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            setTimeZone(Calendar.getInstance().getTimeZone());
        }
    }

    /**
     * 设置时区.
     *
     * @param tz 时区
     */
    public void setTimeZone(TimeZone tz) {
        mapper.setTimeZone(tz);
    }

    /**
     * Java对象转成Json字符串.
     *
     * @param obj Java对象
     * @return Json字符串 string
     */
    public String toJsonString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else {
            try {
                return mapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RTException(e);
            }
        }
    }

    /**
     * Java对象转成JsonNode.
     *
     * @param obj Java对象
     * @return JsonNode json node
     */
    public JsonNode toJson(Object obj) {
        if (obj instanceof String) {
            try {
                return mapper.readTree((String) obj);
            } catch (IOException e) {
                throw new RTIOException(e);
            }
        } else {
            return mapper.valueToTree(obj);
        }
    }

    /**
     * 转成List泛型对象.
     *
     * @param <E>   the type parameter
     * @param obj   源数据，可以是Json字符串、JsonNode或其它Java对象
     * @param clazz 目标对象类型
     * @return 目标对象 list
     */
    public <E> List<E> toList(Object obj, Class<E> clazz) {
        return (List<E>) toGeneric(obj, List.class, clazz);
    }

    /**
     * 转成Set泛型对象.
     *
     * @param <E>   the type parameter
     * @param obj   源数据，可以是Json字符串、JsonNode或其它Java对象
     * @param clazz 目标对象类型
     * @return 目标对象 set
     */
    public <E> Set<E> toSet(Object obj, Class<E> clazz) {
        return (Set<E>) toGeneric(obj, Set.class, clazz);
    }

    /**
     * 转成Map泛型对象.
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param obj        源数据，可以是Json字符串、JsonNode或其它Java对象
     * @param keyClazz   目标对象类型Key
     * @param valueClazz 目标对象类型Value
     * @return 目标对象 map
     */
    public <K, V> Map<K, V> toMap(Object obj, Class<K> keyClazz, Class<V> valueClazz) {
        return (Map<K, V>) toGeneric(obj, Map.class, keyClazz, valueClazz);
    }

    /**
     * 转成泛型对象.
     *
     * @param obj              源数据，可以是Json字符串、JsonNode或其它Java对象
     * @param parametrized     目标对象类型
     * @param parameterClasses 目标对象泛型类型
     * @return 目标对象 object
     */
    public Object toGeneric(Object obj, Class<?> parametrized, Class... parameterClasses) {
        JavaType type = mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        return toGeneric(obj, type);
    }

    private Object toGeneric(Object obj, JavaType type) {
        try {
            if (obj instanceof String) {
                return mapper.readValue((String) obj, type);
            } else if (obj instanceof JsonNode) {
                return mapper.readValue(obj.toString(), type);
            } else {
                return mapper.readValue(mapper.writeValueAsString(obj), type);
            }
        } catch (IOException e) {
            throw new RTIOException(e);
        }
    }

    /**
     * 转成目标对象.
     *
     * @param <E>   the type parameter
     * @param obj   源数据，可以是Json字符串、JsonNode或其它Java对象
     * @param clazz 目标对象类型
     * @return 目标对象 e
     */
    public <E> E toObject(Object obj, Class<E> clazz) {
        try {
            if (obj instanceof String) {
                if (clazz == String.class) {
                    return (E) obj;
                } else {
                    return mapper.readValue((String) obj, clazz);
                }
            } else if (obj instanceof JsonNode) {
                return mapper.readValue(obj.toString(), clazz);
            } else {
                return mapper.readValue(mapper.writeValueAsString(obj), clazz);
            }
        } catch (IOException e) {
            throw new RTIOException(e);
        }
    }

    /**
     * 获取对应路径下的Json.
     *
     * @param jsonNode Json对象
     * @param pathStr  路径
     * @return 对应的Json对象 json node
     */
    public JsonNode path(JsonNode jsonNode, String pathStr) {
        String[] splitPaths = pathStr.split("\\.");
        jsonNode = jsonNode.path(splitPaths[0]);
        if (jsonNode instanceof MissingNode) {
            return null;
        } else if (splitPaths.length == 1) {
            return jsonNode;
        } else {
            return path(jsonNode, pathStr.substring(pathStr.indexOf(".") + 1));
        }
    }

    /**
     * 创建ObjectNode.
     *
     * @return objectNode object node
     */
    public ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    /**
     * 创建ArrayNode.
     *
     * @return arrayNode array node
     */
    public ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    /**
     * 获取Jackson底层操作.
     *
     * @return Jackson ObjectMapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

}
