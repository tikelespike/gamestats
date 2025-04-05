package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.ScriptCreationDTO;
import com.tikelespike.gamestats.api.entities.ScriptDTO;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.ValidationUtils;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.entities.ScriptCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.businesslogic.services.ScriptService;
import com.tikelespike.gamestats.common.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

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
    private static final String API_PATH = "/api/v1/scripts";
    private static final String API_PATH_WITH_SUBPATH = API_PATH + "/";
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
            return ValidationUtils.requestInvalid(validation.getMessage(), API_PATH);
        }

        Script script;
        try {
            script = scriptService.createScript(creationMapper.toBusinessObject(creationRequest));
        } catch (RelatedResourceNotFoundException e) {
            return ValidationUtils.requestInvalid(
                    "At least one of the characters of the script does not exist",
                    API_PATH
            );
        } catch (ResourceNotFoundException e) {
            return ValidationUtils.notFound(API_PATH);
        }

        ScriptDTO transferObject = scriptMapper.toTransferObject(script);
        URI scriptURI = URI.create(API_PATH_WITH_SUBPATH + script.getId());
        return ResponseEntity.created(scriptURI).body(transferObject);
    }

    /**
     * Retrieves all scripts.
     *
     * @return a REST response entity containing all scripts currently known to the system
     */
    @Operation(
            summary = "Retrieves all scripts",
            description = "Retrieves a list of all scripts registered in the system and available for use in "
                    + "games."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieval successful. The response body contains the list of scripts",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ScriptDTO.class)))}
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
    @GetMapping()
    public ResponseEntity<Object> getCharacters() {
        List<Script> scripts = scriptService.getAllScripts();

        List<ScriptDTO> transferObjects = scripts.stream().map(scriptMapper::toTransferObject).toList();
        return ResponseEntity.ok(transferObjects);
    }

    /**
     * Updates a script.
     *
     * @param id script id (must be the same as in the body, if given there)
     * @param scriptDTO script to update
     *
     * @return a REST response entity containing the updated script
     */
    @Operation(
            summary = "Updates a script",
            description =
                    "Updates the details about an existing script, like the characters available in it, its name, or "
                            + "its description. The script has to be created before it"
                            + " can be changed by this endpoint."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Updated the script successfully. The response body contains the updated "
                            + "script.",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ScriptDTO.class)))}
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
                    responseCode = "404",
                    description = "Not found. There exists no resource under the given URI.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Conflict. The resource was deleted concurrently during the processing of this "
                            + "request, or there already exists a newer version of this resource that would be "
                            + "overwritten.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Please try again later. If the issue persists, contact "
                            + "the system administrator or development team.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @PreAuthorize("hasAuthority('STORYTELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCharacter(@PathVariable("id") long id, @RequestBody ScriptDTO scriptDTO) {
        ValidationResult validation = scriptDTO.validateUpdate(id);
        if (!validation.isValid()) {
            return ValidationUtils.requestInvalid(validation.getMessage(), API_PATH_WITH_SUBPATH + id);
        }

        Script scriptUpdate;
        try {
            scriptUpdate = scriptMapper.toBusinessObject(scriptDTO);
        } catch (RelatedResourceNotFoundException e) {
            return ValidationUtils.requestInvalid(
                    "At least one of the characters of the script does not exist",
                    API_PATH_WITH_SUBPATH + id
            );
        }
        Script newScript;
        try {
            newScript = scriptService.updateScript(scriptUpdate);
        } catch (ResourceNotFoundException e) {
            return ValidationUtils.notFound(API_PATH_WITH_SUBPATH + id);
        } catch (StaleDataException e) {
            return ValidationUtils.conflict(API_PATH_WITH_SUBPATH + id);
        }

        ScriptDTO transferObject = scriptMapper.toTransferObject(newScript);
        return ResponseEntity.ok(transferObject);
    }

    /**
     * Deletes a script from the system.
     *
     * @param id the ID of the script to delete
     *
     * @return a REST response entity indicating the result of the operation
     */
    @Operation(
            summary = "Deletes a script",
            description = "Removes a script from the system. If the script does not exist, the operation has no effect."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "204",
                    description = "Script deleted successfully. No content is returned."
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteScript(@PathVariable("id") long id) {
        scriptService.deleteScript(id);
        return ResponseEntity.noContent().build();
    }
}
