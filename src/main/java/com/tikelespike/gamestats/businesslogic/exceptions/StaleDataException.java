package com.tikelespike.gamestats.businesslogic.exceptions;

/**
 * Indicates that data has been concurrently modified and the current request is based on outdated data.
 */
public class StaleDataException extends Exception {

    /**
     * Creates a new StaleDataException.
     */
    public StaleDataException() {
        super();
    }

    /**
     * Creates a new StaleDataException with the given message.
     *
     * @param message the message to display
     */
    public StaleDataException(String message) {
        super(message);
    }

    /**
     * Creates a new StaleDataException with the given message and cause.
     *
     * @param message the message to display
     * @param cause the cause of this exception
     */
    public StaleDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new StaleDataException with the given cause.
     *
     * @param cause the cause of this exception
     */
    public StaleDataException(Throwable cause) {
        super(cause);
    }
}
