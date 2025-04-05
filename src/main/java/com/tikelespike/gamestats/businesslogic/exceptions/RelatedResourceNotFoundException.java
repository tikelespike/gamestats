package com.tikelespike.gamestats.businesslogic.exceptions;

/**
 * Indicates that an object references other objects, but the referenced objects do not exist in the system.
 */
public class RelatedResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new RelatedResourceNotFoundException.
     */
    public RelatedResourceNotFoundException() {
    }

    /**
     * Creates a new RelatedResourceNotFoundException with the given message.
     *
     * @param message the error message
     */
    public RelatedResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new RelatedResourceNotFoundException with the given message and cause.
     *
     * @param message the error message
     * @param cause another throwable that caused this exception
     */
    public RelatedResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new RelatedResourceNotFoundException with the given cause.
     *
     * @param cause another throwable that caused this exception
     */
    public RelatedResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new RelatedResourceNotFoundException with the given message, cause and flags.
     *
     * @param message the error message
     * @param cause another throwable that caused this exception
     * @param enableSuppression whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable or not
     */
    public RelatedResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
