package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the JSON Web Token used for user authentication.
 *
 * @param accessToken the access token
 */
@Schema(name = "JSON Web Token", description = "A token used for authenticating a user. Can be obtained using the "
        + "login endpoint. The token must be included in the Authorization header of all requests that require "
        + "authentication.")
public record JwtDTO(
        @Schema(description = "The access token itself in string form.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                        +
                        ".eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwidXNlcm5hbWUiOiJ0ZXN0QHRlc3QuY29tIiwiZXhwIjoxNzM0OTg3MDA3fQ"
                        + ".WXyFxT2qwWNbsjOVAkaTos6Z_cVioQ2XPqbOXsrsTxQ") String accessToken) {
}
