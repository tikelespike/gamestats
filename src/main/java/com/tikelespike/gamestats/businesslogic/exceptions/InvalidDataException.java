package com.tikelespike.gamestats.businesslogic.exceptions;

/**
 * Indicates that data passed to the service is invalid for any reason.
 */
public class InvalidDataException extends RuntimeException {
    /**
     * Creates a new invalid data exception.
     */
    public InvalidDataException() {
    }

    /**
     * Creates a new exception with the given message.
     *
     * @param message exception message
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Creates a new exception.
     *
     * @param message exception message
     * @param cause cause
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new exception.
     *
     * @param cause cause
     */
    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new exception.
     *
     * @param message exception message
     * @param cause cause
     * @param enableSuppression enableSuppression
     * @param writableStackTrace writableStackTrace
     */
    public InvalidDataException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
