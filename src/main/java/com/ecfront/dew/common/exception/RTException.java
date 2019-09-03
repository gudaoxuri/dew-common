package com.ecfront.dew.common.exception;

/**
 * The type Rt exception.
 *
 * @author gudaoxuri
 */
public class RTException extends RuntimeException {

    /**
     * Instantiates a new Rt exception.
     */
    public RTException() {
    }

    /**
     * Instantiates a new Rt exception.
     *
     * @param message the message
     */
    public RTException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Rt exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RTException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Rt exception.
     *
     * @param cause the cause
     */
    public RTException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Rt exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public RTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
