package com.ecfront.dew.common.exception;

/**
 * The type Runtime IO exception.
 *
 * @author gudaoxuri
 */
public class RTIOException extends RTException {

    /**
     * Instantiates a new Rtio exception.
     */
    public RTIOException() {
    }

    /**
     * Instantiates a new Rtio exception.
     *
     * @param message the message
     */
    public RTIOException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Rtio exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public RTIOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Rtio exception.
     *
     * @param cause the cause
     */
    public RTIOException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Rtio exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public RTIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
