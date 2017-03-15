package com.ecfront.dew.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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

public class JsonHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        setTimeZone(Calendar.getInstance().getTimeZone());
    }

    public static void setTimeZone(TimeZone tz) {
        MAPPER.setTimeZone(tz);
    }

    public static String toJsonString(Object obj) throws RuntimeException {
        if (obj instanceof String) {
            return (String) obj;
        } else {
            try {
                return MAPPER.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static JsonNode toJson(Object obj) throws RuntimeException {
        if (obj instanceof String) {
            try {
                return MAPPER.readTree((String) obj);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return MAPPER.valueToTree(obj);
        }
    }

    public static <E> List<E> toList(Object obj, Class<E> clazz) throws RuntimeException {
        JavaType type = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            if (obj instanceof String) {
                return MAPPER.readValue((String) obj, type);
            } else if (obj instanceof JsonNode) {
                return MAPPER.readValue(obj.toString(), type);
            } else {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E> E toObject(Object obj, Class<E> clazz) throws RuntimeException {
        try {
            if (obj instanceof String) {
                if (clazz == String.class) {
                    return (E) obj;
                } else {
                    return MAPPER.readValue((String) obj, clazz);
                }
            } else if (obj instanceof JsonNode) {
                return MAPPER.readValue(obj.toString(), clazz);
            } else {
                return MAPPER.readValue(MAPPER.writeValueAsString(obj), clazz);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

}
