package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 响应模型，用于统一请求响应处理.
 * <p>
 * 模型由三个属性组成：
 * code：响应编码，同HTTP状态码，200表示成功
 * message：错误描述，当code不为200时用于描述错误信息
 * body：返回的实际对象
 *
 * @param <E> 实际对象类型
 * @author gudaoxuri
 */
public class Resp<E> implements Serializable {

    /**
     * The constant FLAG_CODE.
     */
    public static final String FLAG_CODE = "code";
    /**
     * The constant FLAG_BODY.
     */
    public static final String FLAG_BODY = "body";
    /**
     * The constant FLAG_MESSAGE.
     */
    public static final String FLAG_MESSAGE = "message";

    /**
     * 响应编码，同HTTP状态码，200表示成功.
     */
    private String code;
    /**
     * 错误描述，当code不为200时用于描述错误信息.
     */
    private String message;
    /**
     * 返回的实际对象.
     */
    private E body;

    /**
     * Instantiates a new Resp.
     */
    public Resp() {
    }

    /**
     * Instantiates a new Resp.
     *
     * @param code    the code
     * @param message the message
     * @param body    the body
     */
    public Resp(String code, String message, E body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    /**
     * 返回成功.
     *
     * @param <E>  the type parameter
     * @param body 实际对象
     * @return the resp
     */
    public static <E> Resp<E> success(E body) {
        return new Resp<>(StandardCode.SUCCESS.toString(), "", body);
    }

    /**
     * 返回未找到资源的错误，如数据记录不存在.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> notFound(String message) {
        return new Resp<>(StandardCode.NOT_FOUND.toString(), message, null);
    }

    /**
     * 返回资源冲突的错误，如密码错误.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> conflict(String message) {
        return new Resp<>(StandardCode.CONFLICT.toString(), message, null);
    }

    /**
     * 返回资源被锁定的错误，如当前用户被禁用.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> locked(String message) {
        return new Resp<>(StandardCode.LOCKED.toString(), message, null);
    }

    /**
     * 返回请求格式的错误，如json不合法.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> unsupportedMediaType(String message) {
        return new Resp<>(StandardCode.UNSUPPORTED_MEDIA_TYPE.toString(), message, null);
    }

    /**
     * 返回请求参数的错误，如缺少参数.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> badRequest(String message) {
        return new Resp<>(StandardCode.BAD_REQUEST.toString(), message, null);
    }

    /**
     * 返回请求拒绝的错误.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> forbidden(String message) {
        return new Resp<>(StandardCode.FORBIDDEN.toString(), message, null);
    }

    /**
     * 返回未认证的错误，如资源没有权限访问.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> unAuthorized(String message) {
        return new Resp<>(StandardCode.UNAUTHORIZED.toString(), message, null);
    }

    /**
     * 返回服务器的错误，如捕捉到执行异常.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> serverError(String message) {
        return new Resp<>(StandardCode.INTERNAL_SERVER_ERROR.toString(), message, null);
    }

    /**
     * 返回方法未实现的错误.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> notImplemented(String message) {
        return new Resp<>(StandardCode.NOT_IMPLEMENTED.toString(), message, null);
    }

    /**
     * 返回服务不可用的错误.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> serverUnavailable(String message) {
        return new Resp<>(StandardCode.SERVICE_UNAVAILABLE.toString(), message, null);
    }

    /**
     * 返回未知错误.
     *
     * @param <E>     the type parameter
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> unknown(String message) {
        return new Resp<>(StandardCode.UNKNOWN.toString(), message, null);
    }

    /**
     * 自定义错误.
     *
     * @param <E>     the type parameter
     * @param code    the code
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> customFail(String code, String message) {
        return new Resp<>(code, message, null);
    }

    /**
     * 错误转换，解决java泛型继承问题.
     *
     * @param <E>  the type parameter
     * @param resp 原响应对象
     * @return 转换后的响应对象 resp
     */
    public static <E> Resp<E> error(Resp<?> resp) {
        return new Resp<>(resp.getCode(), resp.getMessage(), null);
    }

    /**
     * 自定义响应.
     *
     * @param <E>     the type parameter
     * @param code    the code
     * @param body    实际对象
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> custom(String code, E body, String message) {
        return new Resp<>(code, message, body);
    }

    /**
     * 自定义响应.
     *
     * @param <E>     the type parameter
     * @param code    the code
     * @param message 错误描述
     * @return the resp
     */
    public static <E> Resp<E> custom(String code, String message) {
        return new Resp<>(code, message, null);
    }

    /**
     * 自定义响应.
     *
     * @param <E>  the type parameter
     * @param code the code
     * @param body 实际对象
     * @return the resp
     */
    public static <E> Resp<E> custom(String code, E body) {
        return new Resp<>(code, "", body);
    }

    /**
     * 响应body转换，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<E> generic(String resp, Class<E> bodyClazz) {
        return generic($.json.toObject(resp, Resp.class), bodyClazz);
    }

    /**
     * 响应body转换，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<E> generic(Resp resp, Class<E> bodyClazz) {
        E body = null;
        if (resp.ok() && resp.getBody() != null) {
            body = $.json.toObject(resp.getBody(), bodyClazz);
        }
        return new Resp<>(resp.getCode(), resp.getMessage(), body);
    }

    /**
     * 响应body转换(列表)，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<List<E>> genericList(String resp, Class<E> bodyClazz) {
        return genericList($.json.toObject(resp, Resp.class), bodyClazz);
    }

    /**
     * 响应body转换(列表)，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<List<E>> genericList(Resp resp, Class<E> bodyClazz) {
        List<E> body = null;
        if (resp.ok() && resp.getBody() != null) {
            body = $.json.toList(resp.getBody(), bodyClazz);
        }
        return new Resp<>(resp.getCode(), resp.getMessage(), body);
    }

    /**
     * 响应body转换(Set)，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<Set<E>> genericSet(String resp, Class<E> bodyClazz) {
        return genericSet($.json.toObject(resp, Resp.class), bodyClazz);
    }

    /**
     * 响应body转换(Set)，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<Set<E>> genericSet(Resp resp, Class<E> bodyClazz) {
        Set<E> body = null;
        if (resp.ok() && resp.getBody() != null) {
            body = $.json.toSet(resp.getBody(), bodyClazz);
        }
        return new Resp<>(resp.getCode(), resp.getMessage(), body);
    }

    /**
     * 响应body转换(分页)，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<Page<E>> genericPage(String resp, Class<E> bodyClazz) {
        return genericPage($.json.toObject(resp, Resp.class), bodyClazz);
    }

    /**
     * 响应body转换(分页)，将body类型A转换成类型B.
     *
     * @param <E>       the type parameter
     * @param resp      源响应
     * @param bodyClazz 目标body类型
     * @return 目标响应 resp
     */
    public static <E> Resp<Page<E>> genericPage(Resp resp, Class<E> bodyClazz) {
        Page<E> body = null;
        if (resp.ok() && resp.getBody() != null) {
            body = $.json.toObject(resp.getBody(), Page.class);
            if (body.getObjects() != null) {
                body.setObjects(body.getObjects().stream().map(i -> $.json.toObject(i, bodyClazz)).collect(Collectors.toList()));
            } else {
                body.setObjects(null);
            }
        }
        return new Resp<>(resp.getCode(), resp.getMessage(), body);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public E getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(E body) {
        this.body = body;
    }

    /**
     * 返回是否成功.
     *
     * @return the boolean
     */
    public boolean ok() {
        return Objects.equals(this.code, StandardCode.SUCCESS.toString());
    }

    /**
     * 声明返回需要降级.
     *
     * @return the resp
     */
    public Resp fallback() {
        if (true) {
            throw new FallbackException(this);
        }
        // 使用返回值是为了保持结构统一
        return this;
    }

    /**
     * The type Fallback exception.
     *
     * @author gudaoxuri
     */
    public static class FallbackException extends RTException {

        /**
         * Instantiates a new Fallback exception.
         *
         * @param resp the resp
         */
        public FallbackException(Resp<?> resp) {
            super($.json.toJsonString(resp));
        }

    }

}
