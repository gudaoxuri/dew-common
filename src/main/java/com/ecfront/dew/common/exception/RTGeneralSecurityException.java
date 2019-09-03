package com.ecfront.dew.common.exception;

/**
 * The type Rt general security exception.
 *
 * @author gudaoxuri
 */
public class RTGeneralSecurityException extends RTException {

    /**
     * Instantiates a new Rt general security exception.
     */
    public RTGeneralSecurityException() {
    }

    /**
     * Instantiates a new Rt general security exception.
     *
     * @param message the message
     */
    public RTGeneralSecurityException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Rt general security exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RTGeneralSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Rt general security exception.
     *
     * @param cause the cause
     */
    public RTGeneralSecurityException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Rt general security exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public RTGeneralSecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
