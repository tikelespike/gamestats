package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the user login request.
 *
 * @param email user email address
 * @param password user password
 */
@Schema(
        name = "Login Request",
        description = "A user login request containing the login credentials."
)
public record SignInDTO(
        @Schema(
                description = "User's email address",
                example = "user@example.com"
        ) String email,
        @Schema(
                description = "User's password",
                example = "password123"
        ) String password
) {
}
