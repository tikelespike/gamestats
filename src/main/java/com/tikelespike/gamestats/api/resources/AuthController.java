package com.tikelespike.gamestats.api.resources;

import com.tikelespike.gamestats.api.entities.JwtDTO;
import com.tikelespike.gamestats.api.entities.SignInDTO;
import com.tikelespike.gamestats.api.entities.SignUpDTO;
import com.tikelespike.gamestats.api.mapper.SignupMapper;
import com.tikelespike.gamestats.api.security.TokenProvider;
import com.tikelespike.gamestats.businesslogic.UserService;
import com.tikelespike.gamestats.businesslogic.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
        name = "User Management",
        description = "Operations for managing application users"
)
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService service;
    private final TokenProvider tokenService;
    private final SignupMapper signupMapper;

    /**
     * Creates a new AuthController. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param authenticationManager the authentication manager validating user credentials
     * @param service the authentication service handling sign-up and sign-in
     * @param tokenService the token service generating JWT tokens
     * @param signupMapper the mapper for converting between sign-up data transfer objects and business objects
     */
    public AuthController(AuthenticationManager authenticationManager, UserService service, TokenProvider tokenService,
                          SignupMapper signupMapper) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.tokenService = tokenService;
        this.signupMapper = signupMapper;
    }

    /**
     * Sign up a new user.
     *
     * @param data the sign-up data transfer object containing user credentials
     *
     * @return a REST response entity (201 CREATED if the sign-up was successful)
     */
    @Operation(summary = "Sign up a new user")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "201",
                    description = "New user created. Use the signin endpoint to retrieve an authentication token."
            )}
    )
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Parameter(description = "Sign-up request details") SignUpDTO data) {
        service.signUp(signupMapper.toBusinessObject(data));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Sign in an existing user.
     *
     * @param data the sign-in data transfer object containing user credentials
     *
     * @return a response entity containing the JWT token
     */
    @Operation(summary = "Sign in an existing user")
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Signed in successfully. The response body contains the JWT token.",
                    content = {@Content(schema = @Schema(implementation = JwtDTO.class))}
            )}
    )
    @PostMapping("/signin")
    public ResponseEntity<JwtDTO> signIn(@RequestBody @Schema(description = "Sign-in request details") SignInDTO data) {
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);
        String accessToken = tokenService.generateAccessToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new JwtDTO(accessToken));
    }
}
