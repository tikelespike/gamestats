package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.ScriptCreationDTO;
import com.tikelespike.gamestats.api.entities.ScriptDTO;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.ValidationUtils;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.entities.ScriptCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.services.ScriptService;
import com.tikelespike.gamestats.common.Mapper;
import io.swagger.v3.oas.annotations.Operation;
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

import java.net.URI;

/**
 * REST controller for managing scripts. A script is a collection of characters that defines which characters may be in
 * play during a game.
 */
@RestController
@RequestMapping("/api/v1/scripts")
@Tag(
        name = "Script Management",
        description = "Operations for managing collections of characters (scripts)"
)
public class ScriptController {
    private final ScriptService scriptService;
    private final Mapper<Script, ScriptDTO> scriptMapper;
    private final Mapper<ScriptCreationRequest, ScriptCreationDTO> creationMapper;

    /**
     * Creates a new ScriptController. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param scriptService the business layer script service to use for managing scripts. May not be null.
     * @param scriptMapper maps between script business objects and their REST representations
     * @param creationMapper maps between script creation requests and their REST representations
     */
    public ScriptController(ScriptService scriptService, Mapper<Script, ScriptDTO> scriptMapper,
                            Mapper<ScriptCreationRequest, ScriptCreationDTO> creationMapper) {
        this.scriptService = scriptService;
        this.scriptMapper = scriptMapper;
        this.creationMapper = creationMapper;
    }

    /**
     * Creates a new script.
     *
     * @param creationRequest REST dto containing the data
     *
     * @return a REST response entity containing the new script
     */
    @Operation(
            summary = "Creates a new script",
            description = "Creates a new in-game script that may be used in games. A script is a collection of "
                    + "characters that defines which characters may be in play during a game."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "201",
                    description = "Created script successfully. The response body contains the newly created "
                            + "script.",
                    content = {@Content(schema = @Schema(implementation = ScriptDTO.class))}
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
    @PreAuthorize("hasAuthority('STORYTELLER')")
    @PostMapping()
    public ResponseEntity<Object> createScript(@RequestBody ScriptCreationDTO creationRequest) {
        ValidationResult validation = creationRequest.validate();
        if (!validation.isValid()) {
            return ValidationUtils.requestInvalid(validation.getMessage(), "/api/v1/scripts");
        }

        Script script;
        try {
            script = scriptService.createScript(creationMapper.toBusinessObject(creationRequest));
        } catch (ResourceNotFoundException e) {
            return ValidationUtils.requestInvalid(
                    "The script creation request contains a character id that does not exist.",
                    "/api/v1/scripts"
            );
        }

        ScriptDTO transferObject = scriptMapper.toTransferObject(script);
        URI scriptURI = URI.create("/api/v1/scripts/" + script.getId());
        return ResponseEntity.created(scriptURI).body(transferObject);
    }
}
