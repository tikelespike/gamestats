package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the JSON Web Token used for user authentication and information about the logged in user.
 *
 * @param accessToken the access token
 * @param userId unique numerical identifier of the signed-in user
 */
@Schema(
        name = "Login confirmation",
        description = "A token used for authenticating a user as well as the "
                + "corresponding user id. Can be obtained using the "
                + "login endpoint. The token must be included in the Authorization header of all requests that require "
                + "authentication."
)
public record SignInInfoDTO(
        @Schema(
                description = "The access token itself in string form.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                        + ".eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwidXNlcm5hbWUiOiJ0ZXN0QHRlc3QuY29tIiwiZXhwIjoxNzM0OTg3MDA3fQ"
                        + ".WXyFxT2qwWNbsjOVAkaTos6Z_cVioQ2XPqbOXsrsTxQ"
        ) String accessToken,
        @Schema(
                description = "The unique numerical identifier of the user that was logged in.",
                example = "42"
        ) Long userId
) {
}
