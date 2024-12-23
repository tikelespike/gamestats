package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the user sign-up request.
 *
 * @param email user email address
 * @param password user password
 */
@Schema(name = "Sign Up Request", description = "A user sign-up request containing the registration credentials.")
public record SignUpDTO(
        @Schema(description = "User's human-readable name (preferably real-world name)",
                example = "Max Mustermann") String name,
        @Schema(description = "User's email address", example = "user@example.com") String email,
        @Schema(description = "User's password", example = "password123") String password) {
}
