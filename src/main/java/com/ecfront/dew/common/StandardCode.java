package com.ecfront.dew.common;

/**
 * The enum Standard code.
 *
 * @author gudaoxuri
 */
public enum StandardCode {
    /**
     * Success standard code.
     */
    SUCCESS("200"),
    /**
     * Bad request standard code.
     */
    BAD_REQUEST("400"),
    /**
     * Unauthorized standard code.
     */
    UNAUTHORIZED("401"),
    /**
     * Forbidden standard code.
     */
    FORBIDDEN("403"),
    /**
     * Not found standard code.
     */
    NOT_FOUND("404"),
    /**
     * Conflict standard code.
     */
    CONFLICT("409"),
    /**
     * Locked standard code.
     */
    LOCKED("423"),
    /**
     * Unsupported media type standard code.
     */
    UNSUPPORTED_MEDIA_TYPE("415"),
    /**
     * Internal server error standard code.
     */
    INTERNAL_SERVER_ERROR("500"),
    /**
     * Not implemented standard code.
     */
    NOT_IMPLEMENTED("501"),
    /**
     * Service unavailable standard code.
     */
    SERVICE_UNAVAILABLE("503"),
    /**
     * Unknown standard code.
     */
    UNKNOWN("-1");

    private String code;

    StandardCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
