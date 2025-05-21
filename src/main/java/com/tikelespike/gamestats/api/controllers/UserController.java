package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.UserCreationDTO;
import com.tikelespike.gamestats.api.entities.UserDTO;
import com.tikelespike.gamestats.api.mapper.UserCreationMapper;
import com.tikelespike.gamestats.api.mapper.UserMapper;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.ValidationUtils;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.exceptions.InvalidDataException;
import com.tikelespike.gamestats.businesslogic.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;

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
    private static final String API_PATH = "/api/v1/users";
    private static final String API_PATH_WITH_SUBPATH = API_PATH + "/";

    private final UserService service;
    private final UserCreationMapper userCreationMapper;
    private final UserMapper userMapper;

    /**
     * Creates a new UserController. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param service the authentication service handling sign-up and sign-in
     * @param userCreationMapper the mapper for converting between sign-up data transfer objects and business
     *         objects
     * @param userMapper the mapper for converting between user business objects and their REST transfer
     */
    public UserController(UserService service, UserCreationMapper userCreationMapper, UserMapper userMapper) {
        this.service = service;
        this.userCreationMapper = userCreationMapper;
        this.userMapper = userMapper;
    }

    /**
     * Create a new user.
     *
     * @param creationRequest the sign-up data transfer object containing user credentials
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
            @RequestBody @Parameter(description = "User creation request details") UserCreationDTO creationRequest) {
        ValidationResult validation = creationRequest.validate();
        if (!validation.isValid()) {
            return ValidationUtils.requestInvalid(validation.getMessage(), API_PATH);
        }

        User user;
        try {
            user = service.createUser(userCreationMapper.toBusinessObject(creationRequest));
        } catch (InvalidDataException e) {
            return ValidationUtils.requestInvalid(e.getMessage(), API_PATH);
        }

        UserDTO transferObject = userMapper.toTransferObject(user);
        URI userURI = URI.create(API_PATH_WITH_SUBPATH + user.getId());
        return ResponseEntity.created(userURI).body(transferObject);
    }

    /**
     * Retrieves all users.
     *
     * @return a REST response entity containing all users currently known to the system
     */
    @Operation(
            summary = "Retrieves all users",
            description = "Retrieves a list of all users registered in the system."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieval successful. The response body contains the list of users",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))}
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
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping()
    public ResponseEntity<Object> getUsers() {
        List<User> users = service.getAllUsers();
        List<UserDTO> transferObjects = users.stream().map(userMapper::toTransferObject).toList();
        return ResponseEntity.ok(transferObjects);
    }

    /**
     * Deletes a user from the system.
     *
     * @param id the ID of the user to delete
     *
     * @return a REST response entity indicating the result of the operation
     */
    @Operation(
            summary = "Deletes a user",
            description = "Removes a user from the system. If the user does not exist, the operation has no effect."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully. No content is returned."
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
