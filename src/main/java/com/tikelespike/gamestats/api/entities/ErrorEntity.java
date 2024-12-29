package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.Instant;


/**
 * REST transfer object for error responses.
 *
 * @param timestamp the time when the error occurred
 * @param status the HTTP status code of the error
 * @param error the HTTP status as human-readable string
 * @param message a human-readable error message
 * @param path the path of the resource that caused the error
 */
public record ErrorEntity(
        @Schema(
                description = "Timestamp of the error occurrence in ISO 8601 format.",
                example = "2024-07-01T12:34:56.789Z"
        ) Instant timestamp,
        @Schema(
                description = "HTTP status code of the error.",
                example = "400"
        ) int status,
        @Schema(
                description = "Human-readable HTTP status.",
                example = "Bad Request"
        ) String error,
        @Schema(
                description = "Specific error message.",
                example = "The player name is required."
        ) String message,
        @Schema(
                description = "Path of the resource that caused the error.",
                example = "/api/v1/players"
        ) String path
) {
    /**
     * Creates an error entity for a 400 Bad Request error.
     *
     * @param message the specific error message
     * @param path the path of the resource that caused the error
     *
     * @return the error entity
     */
    public static ErrorEntity badRequest(String message, String path) {
        return new ErrorEntity(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", message, path);
    }
}
