package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.CharacterCreationDTO;
import com.tikelespike.gamestats.api.entities.CharacterDTO;
import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.ValidationUtils;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.businesslogic.services.CharacterService;
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
 * REST controller for managing characters. A character is the role a player takes on within a single game, and what
 * provides the player with abilities. Characters are published by the official Blood on the Clocktower developers, and
 * can be added to this applications database by users with the appropriate permissions. This also allows for the
 * creation of self-made characters that are not part of the official game.
 */
@RestController
@RequestMapping("/api/v1/characters")
@Tag(
        name = "Character Management",
        description = "Operations for managing in-game characters"
)
public class CharacterController {
    private final CharacterService characterService;
    private final Mapper<CharacterCreationRequest, CharacterCreationDTO> creationMapper;
    private final Mapper<Character, CharacterDTO> characterMapper;

    /**
     * Creates a new character controller. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param characterService the business layer character service to use for managing characters
     * @param creationMapper the mapper for converting between character creation requests and their JSON
     *         representation
     * @param characterMapper the mapper for converting between character business objects and their JSON
     *         representation
     */
    public CharacterController(CharacterService characterService,
                               Mapper<CharacterCreationRequest, CharacterCreationDTO> creationMapper,
                               Mapper<Character, CharacterDTO> characterMapper) {
        this.characterService = characterService;
        this.creationMapper = creationMapper;
        this.characterMapper = characterMapper;
    }

    /**
     * Creates a new character.
     *
     * @param creationRequest REST dto containing the data
     *
     * @return a REST response entity containing the new character
     */
    @Operation(
            summary = "Creates a new character",
            description = "Creates a new in-game character that can be used in scripts, such that players can play "
                    + "them during games."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "201",
                    description = "Created character successfully. The response body contains the newly created "
                            + "character.",
                    content = {@Content(schema = @Schema(implementation = CharacterDTO.class))}
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
    public ResponseEntity<Object> createCharacter(@RequestBody CharacterCreationDTO creationRequest) {
        ValidationResult validation = creationRequest.validate();
        if (!validation.isValid()) {
            return ValidationUtils.requestInvalid(validation.getMessage(), "/api/v1/characters");
        }

        Character character = characterService.createCharacter(creationMapper.toBusinessObject(creationRequest));

        CharacterDTO transferObject = characterMapper.toTransferObject(character);
        URI characterURI = URI.create("/api/v1/characters/" + character.getId());
        return ResponseEntity.created(characterURI).body(transferObject);
    }

    /**
     * Retrieves all characters.
     *
     * @return a REST response entity containing all characters currently known to the system
     */
    @Operation(
            summary = "Retrieves all characters",
            description = "Retrieves a list of all characters registered in the system and available for use in "
                    + "scripts and games."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieval successful. The response body contains the list of characters",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CharacterDTO.class)))}
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
        List<Character> characters = characterService.getAllCharacters();

        List<CharacterDTO> transferObjects = characters.stream().map(characterMapper::toTransferObject).toList();
        return ResponseEntity.ok(transferObjects);
    }

    /**
     * Updates a character.
     *
     * @param id character id (must be the same as in the body, if given there)
     * @param characterDTO character to update
     *
     * @return a REST response entity containing the updated character
     */
    @Operation(
            summary = "Updates a character",
            description = "Updates the details about an existing character. The character has to be created before it"
                    + " can be changed by this endpoint."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Updated the character successfully. The response body contains the updated "
                            + "character.",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CharacterDTO.class)))}
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
    public ResponseEntity<Object> updateCharacter(@PathVariable("id") long id, @RequestBody CharacterDTO characterDTO) {
        ValidationResult validation = characterDTO.validateUpdate(id);
        if (!validation.isValid()) {
            return ValidationUtils.requestInvalid(validation.getMessage(), "/api/v1/characters/" + id);
        }

        Character characterUpdate = characterMapper.toBusinessObject(characterDTO);
        Character newCharacter;
        try {
            newCharacter = characterService.updateCharacter(characterUpdate);
        } catch (ResourceNotFoundException e) {
            return ValidationUtils.notFound("/api/v1/characters/" + id);
        } catch (StaleDataException e) {
            return ValidationUtils.conflict("/api/v1/characters/" + id);
        }

        CharacterDTO transferObject = characterMapper.toTransferObject(newCharacter);
        return ResponseEntity.ok(transferObject);
    }

    /**
     * Deletes a character.
     *
     * @param id id of the character to delete
     *
     * @return an HTTP response code indicating the success of the request
     */
    @Operation(
            summary = "Deletes a character",
            description = "Deletes a character by its id, if it exists. If the character does not exist, this method "
                    + "has no effect and will return a 204 response code nonetheless."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "204",
                    description = "Deleted the character successfully, or there was no character with that id to "
                            + "begin with."
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
    public ResponseEntity<Object> deleteCharacter(@PathVariable("id") long id) {
        characterService.deleteCharacter(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes multiple characters in a single request. The deletion is atomic - either all characters are deleted, or
     * none are. This is useful for bulk removing characters from the system.
     *
     * @param ids list of character IDs to delete
     *
     * @return an HTTP response code indicating the success of the request
     */
    @Operation(
            summary = "Deletes multiple characters",
            description = "Deletes multiple characters by their IDs in a single atomic operation. If any character "
                    + "deletion fails, none of the characters will be deleted. This is useful for bulk removing "
                    + "characters from the system."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "204",
                    description = "Deleted the characters successfully, or there were no characters with those IDs to "
                            + "begin with."
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
    @DeleteMapping("/batch")
    public ResponseEntity<Object> deleteCharacters(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ValidationUtils.requestInvalid("No character IDs provided", "/api/v1/characters/batch");
        }

        characterService.deleteCharacters(ids);

        return ResponseEntity.noContent().build();
    }

    /**
     * Creates multiple characters in a single request. The creation is atomic - either all characters are created, or
     * none are. This is useful for bulk importing characters or creating multiple related characters at once.
     *
     * @param creationRequests list of character creation requests
     *
     * @return a REST response entity containing all newly created characters
     */
    @Operation(
            summary = "Creates multiple characters",
            description = "Creates multiple in-game characters in a single atomic operation. If any character creation "
                    + "fails, none of the characters will be created. This is useful for bulk importing characters or "
                    + "creating multiple related characters at once."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "201",
                    description = "Created characters successfully. The response body contains the newly created "
                            + "characters.",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CharacterDTO.class)))}
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
    @PostMapping("/batch")
    public ResponseEntity<Object> createCharacters(@RequestBody List<CharacterCreationDTO> creationRequests) {
        for (CharacterCreationDTO request : creationRequests) {
            ValidationResult validation = request.validate();
            if (!validation.isValid()) {
                return ValidationUtils.requestInvalid("At least one character is invalid: " + validation.getMessage(),
                        "/api/v1/characters/batch");
            }
        }

        List<CharacterCreationRequest> businessRequests = creationRequests.stream()
                .map(creationMapper::toBusinessObject)
                .toList();

        List<Character> characters = characterService.createCharacters(businessRequests);

        List<CharacterDTO> transferObjects = characters.stream()
                .map(characterMapper::toTransferObject)
                .toList();

        return ResponseEntity.created(URI.create("/api/v1/characters/batch")).body(transferObjects);
    }
}
