package com.ecfront.dew.common.exception;

/**
 * The type Rt unsupported encoding exception.
 *
 * @author gudaoxuri
 */
public class RTUnsupportedEncodingException extends RTException {

    /**
     * Instantiates a new Rt unsupported encoding exception.
     */
    public RTUnsupportedEncodingException() {
    }

    /**
     * Instantiates a new Rt unsupported encoding exception.
     *
     * @param message the message
     */
    public RTUnsupportedEncodingException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Rt unsupported encoding exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RTUnsupportedEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Rt unsupported encoding exception.
     *
     * @param cause the cause
     */
    public RTUnsupportedEncodingException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Rt unsupported encoding exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public RTUnsupportedEncodingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
