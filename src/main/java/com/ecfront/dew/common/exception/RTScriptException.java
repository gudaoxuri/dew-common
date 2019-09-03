package com.ecfront.dew.common.exception;

/**
 * The type Rt script exception.
 *
 * @author gudaoxuri
 */
public class RTScriptException extends RTException {

    /**
     * Instantiates a new Rt script exception.
     */
    public RTScriptException() {
    }

    /**
     * Instantiates a new Rt script exception.
     *
     * @param message the message
     */
    public RTScriptException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Rt script exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RTScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Rt script exception.
     *
     * @param cause the cause
     */
    public RTScriptException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Rt script exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public RTScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
