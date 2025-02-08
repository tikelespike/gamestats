package com.tikelespike.gamestats.api.validation;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for input validation.
 */
public final class ValidationUtils {
    private ValidationUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Convenience method to create a response entity for a bad request.
     *
     * @param message message describing the error
     * @param path path of the request that caused the error
     *
     * @return a response entity with status code 400 and a descriptive body
     */
    public static ResponseEntity<Object> requestInvalid(String message, String path) {
        return ResponseEntity.badRequest().body(ErrorEntity.badRequest(message, path));
    }

    /**
     * Convenience method to create a response entity for a not found error.
     *
     * @param path path of the request that caused the error
     *
     * @return a response entity with status code 404 and a descriptive body
     */
    public static ResponseEntity<Object> notFound(String path) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorEntity.notFound(path));
    }
}
