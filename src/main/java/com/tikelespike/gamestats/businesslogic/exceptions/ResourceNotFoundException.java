package com.tikelespike.gamestats.businesslogic.exceptions;

/**
 * Indicates that a requested resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE = "No resource with id %d found.";

    /**
     * Creates a new ResourceNotFoundException with a message indicating that the resource with the given id was not
     * found.
     *
     * @param id the id of the resource that was not found
     */
    public ResourceNotFoundException(Long id) {
        this(MESSAGE.formatted(id));
    }

    /**
     * Creates a new ResourceNotFoundException with the given message.
     *
     * @param message the message to display
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
