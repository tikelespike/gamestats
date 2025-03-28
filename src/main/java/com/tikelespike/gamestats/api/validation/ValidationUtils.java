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

    /**
     * Convenience method to create a response entity for a conflict error.
     *
     * @param path path of the request that caused the error
     *
     * @return a response entity with status code 409 and a descriptive body
     */
    public static ResponseEntity<Object> conflict(String path) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorEntity.conflict(
                "The resource you are trying to update or delete has already been updated or deleted by another "
                        + "request.",
                path));
    }

    /**
     * Convenience method to create a response entity for an invalid response from an upstream dependency server.
     *
     * @param message message describing the error
     * @param path path of the request that caused the error
     *
     * @return a response entity with status code 502 and a descriptive body
     */
    public static ResponseEntity<Object> upstreamError(String message, String path) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ErrorEntity.badGateway(message, path));
    }
}
