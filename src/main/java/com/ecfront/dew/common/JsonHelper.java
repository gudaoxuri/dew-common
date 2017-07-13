package com.ecfront.dew.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Json与Java对象互转<br/>
 * <p>
 * 为方便在Java8 Stream中使用，操作返回的异常都是运行时异常
 */
public class JsonHelper {

    private ObjectMapper mapper;

    JsonHelper() {
        if (dependencyHelper.hasDependency("com.fasterxml.jackson.core.JsonProcessingException")) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            setTimeZone(Calendar.getInstance().getTimeZone());
        }
    }

    /**
     * 设置时区
     *
     * @param tz 时区
     */
    public void setTimeZone(TimeZone tz) {
        mapper.setTimeZone(tz);
    }

    /**
     * Java对象转成Json字符串
     *
     * @param obj Java对象
     * @return Json字符串
     * @throws RuntimeException
     */
    public String toJsonString(Object obj) throws RuntimeException {
        if (obj instanceof String) {
            return (String) obj;
        } else {
            try {
                return mapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Java对象转成JsonNode
     *
     * @param obj Java对象
     * @return JsonNode
     * @throws RuntimeException
     */
    public JsonNode toJson(Object obj) throws RuntimeException {
        if (obj instanceof String) {
            try {
                return mapper.readTree((String) obj);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return mapper.valueToTree(obj);
        }
    }

    /**
     * 转成List泛型对象
     *
     * @param obj   源数据，可以是Json字符串、JsonNode或其它Java对象
     * @param clazz 目标对象类型
     * @return 目标对象
     * @throws RuntimeException
     */
    public <E> List<E> toList(Object obj, Class<E> clazz) throws RuntimeException {
        JavaType type = mapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            if (obj instanceof String) {
                return mapper.readValue((String) obj, type);
            } else if (obj instanceof JsonNode) {
                return mapper.readValue(obj.toString(), type);
            } else {
                return mapper.readValue(mapper.writeValueAsString(obj), type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转成目标对象
     *
     * @param obj   源数据，可以是Json字符串、JsonNode或其它Java对象
     * @param clazz 目标对象类型
     * @return 目标对象
     * @throws RuntimeException
     */
    public <E> E toObject(Object obj, Class<E> clazz) throws RuntimeException {
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
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建ObjectNode
     *
     * @return objectNode
     */
    public ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    /**
     * 创建ArrayNode
     *
     * @return arrayNode
     */
    public ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    /**
     * 获取Jackson底层操作
     *
     * @return Jackson ObjectMapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

}
