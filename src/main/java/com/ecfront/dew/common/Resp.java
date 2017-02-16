package com.ecfront.dew.common;

import java.util.Objects;

public class Resp<E> {

    private static final String FLAG_CODE = "code";
    private static final String FLAG_BODY = "body";
    private static final String FLAG_MESSAGE = "message";

    private String code;
    private String message;
    private E body;

    public  Resp() {}

    public Resp(String code, String message, E body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public boolean ok(){
        return Objects.equals(this.code, StandardCode.SUCCESS.toString());
    }

    public static Resp<Void> error(Resp<?> resp){
        return new Resp<>(resp.getCode(),resp.getMessage(),null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getBody() {
        return body;
    }

    public void setBody(E body) {
        this.body = body;
    }
    
    public static <E> Resp<E> success(E body) {
        return new Resp<>(StandardCode.SUCCESS.toString(), "", body);
    }

    public static <E> Resp<E> notFound(String message) {
        return new Resp<>(StandardCode.NOT_FOUND.toString(), message, null);
    }

    public static <E> Resp<E> conflict(String message) {
        return new Resp<>(StandardCode.CONFLICT.toString(), message, null);
    }

    public static <E> Resp<E> locked(String message) {
        return new Resp<>(StandardCode.LOCKED.toString(), message, null);
    }

    public static <E> Resp<E> unsupportedMediaType(String message) {
        return new Resp<>(StandardCode.UNSUPPORTED_MEDIA_TYPE.toString(), message, null);
    }

    public static <E> Resp<E> badRequest(String message) {
        return new Resp<>(StandardCode.BAD_REQUEST.toString(), message, null);
    }

    public static <E> Resp<E> forbidden(String message) {
        return new Resp<>(StandardCode.FORBIDDEN.toString(), message, null);
    }

    public static <E> Resp<E> unAuthorized(String message) {
        return new Resp<>(StandardCode.UNAUTHORIZED.toString(), message, null);
    }

    public static <E> Resp<E> serverError(String message) {
        return new Resp<>(StandardCode.INTERNAL_SERVER_ERROR.toString(), message, null);
    }

    public static <E> Resp<E> notImplemented(String message) {
        return new Resp<>(StandardCode.NOT_IMPLEMENTED.toString(), message, null);
    }

    public static <E> Resp<E> serverUnavailable(String message) {
        return new Resp<>(StandardCode.SERVICE_UNAVAILABLE.toString(), message, null);
    }

    public static <E> Resp<E> unknown(String message) {
        return new Resp<>(StandardCode.UNKNOWN.toString(), message, null);
    }

    public static <E> Resp<E> customFail(String code, String message) {
        return new Resp<>(code, message, null);
    }

}
