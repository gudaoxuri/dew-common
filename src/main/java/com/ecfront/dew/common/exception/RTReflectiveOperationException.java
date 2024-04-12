package com.ecfront.dew.common.exception;

/**
 * The type Rt reflective operation exception.
 *
 * @author gudaoxuri
 */
public class RTReflectiveOperationException extends RTException {


    /**
     * Instantiates a new Rt reflective operation exception.
     */
    public RTReflectiveOperationException() {
    }

    /**
     * Instantiates a new Rt reflective operation exception.
     *
     * @param message the message
     */
    public RTReflectiveOperationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Rt reflective operation exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RTReflectiveOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Rt reflective operation exception.
     *
     * @param cause the cause
     */
    public RTReflectiveOperationException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Rt reflective operation exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public RTReflectiveOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
