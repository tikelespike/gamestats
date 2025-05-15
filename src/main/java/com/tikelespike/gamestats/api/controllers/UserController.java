package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.UserCreationDTO;
import com.tikelespike.gamestats.api.mapper.UserCreationMapper;
import com.tikelespike.gamestats.businesslogic.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user authentication.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(
        name = "User Management",
        description = "Operations for managing application users"
)
public class UserController {
    private final UserService service;
    private final UserCreationMapper userCreationMapper;

    /**
     * Creates a new UserController. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param service the authentication service handling sign-up and sign-in
     * @param userCreationMapper the mapper for converting between sign-up data transfer objects and business
     *         objects
     */
    public UserController(UserService service, UserCreationMapper userCreationMapper) {
        this.service = service;
        this.userCreationMapper = userCreationMapper;
    }

    /**
     * Create a new user.
     *
     * @param data the sign-up data transfer object containing user credentials
     *
     * @return a REST response entity (201 CREATED if the sign-up was successful)
     */
    @Operation(
            summary = "Creates a new user",
            description = "Creates a new user account that can be used to log into this application. Pass the "
                    + "credentials in the request body. The response body contains the newly created user."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "201",
                    description = "Created user successfully."
            ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request. The response body contains an error message.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. Your session has expired or you are not logged in. Please sign in "
                            + "again.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. You do not have the necessary permissions to perform this request. "
                            + "Please sign in with an account that has the necessary permissions.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Please try again later. If the issue persists, contact "
                            + "the system administrator or development team.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<Object> createUser(
            @RequestBody @Parameter(description = "User creation request details") UserCreationDTO data) {
        service.createUser(userCreationMapper.toBusinessObject(data));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
