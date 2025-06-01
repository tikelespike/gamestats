package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.SignInDTO;
import com.tikelespike.gamestats.api.entities.SignInInfoDTO;
import com.tikelespike.gamestats.api.security.TokenProvider;
import com.tikelespike.gamestats.businesslogic.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = "User Authentication",
        description = "Operations for logging in users"
)
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenService;

    /**
     * Creates a new UserController. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param authenticationManager the authentication manager validating user credentials
     * @param tokenService the token service generating JWT tokens
     */
    public AuthController(AuthenticationManager authenticationManager, TokenProvider tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     * Sign in an existing user.
     *
     * @param data the sign-in data transfer object containing user credentials
     *
     * @return a response entity containing the JWT token
     */
    @Operation(
            summary = "Signs in an existing user",
            description = "Logs into a user account and returns a JWT token for authentication. Pass that token in "
                    + "the header of all requests from the logged in user as the bearer token. To create a user "
                    + "account, use the users endpoint."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Logged in successfully. The response body contains the JWT token.",
                    content = {@Content(schema = @Schema(implementation = SignInInfoDTO.class))}
            ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request. The response body contains an error message.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Please try again later. If the issue persists, contact "
                            + "the system administrator or development team.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @PostMapping("/signin")
    public ResponseEntity<SignInInfoDTO> signIn(
            @RequestBody @Schema(description = "Sign-in request details") SignInDTO data) {
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);
        User user = (User) authentication.getPrincipal();
        String accessToken = tokenService.generateAccessToken(user);
        return ResponseEntity.ok(new SignInInfoDTO(accessToken, user.getId()));
    }
}
