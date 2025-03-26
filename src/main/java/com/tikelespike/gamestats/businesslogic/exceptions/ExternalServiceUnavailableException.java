package com.tikelespike.gamestats.businesslogic.exceptions;

/**
 * Indicates that an external service that is depended on is currently unavailable or has returned an unexpected
 * response.
 */
public class ExternalServiceUnavailableException extends Exception {
    /**
     * Creates a new ExternalServiceUnavailableException with the given message.
     *
     * @param message error message
     */
    public ExternalServiceUnavailableException(String message) {
        super(message);
    }

    /**
     * Creates a new ExternalServiceUnavailableException with the given message and cause.
     *
     * @param message error message
     * @param cause the cause of this exception
     */
    public ExternalServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
