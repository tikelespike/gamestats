package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.CharacterCreationDTO;
import com.tikelespike.gamestats.api.entities.CharacterDTO;
import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.validation.ValidationUtils;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.ExternalServiceUnavailableException;
import com.tikelespike.gamestats.businesslogic.services.CharacterService;
import com.tikelespike.gamestats.businesslogic.services.OfficialCharactersGateway;
import com.tikelespike.gamestats.common.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for accessing the official characters provided externally.
 */
@RestController
@RequestMapping("/api/v1/officialtool")
@Tag(
        name = "Official Resources API",
        description = "Access officially published game content."
)
public class OfficialToolController {

    private final OfficialCharactersGateway officialCharactersGateway;
    private final Mapper<CharacterCreationRequest, CharacterCreationDTO> characterMapper;
    private final CharacterService characterService;
    private final Mapper<Character, CharacterDTO> characterUpdateMapper;


    /**
     * Creates a new controller for accessing the official script tool. This is usually done by the Spring framework,
     * which manages the controller's lifecycle and injects the required dependencies.
     *
     * @param officialCharactersGateway implementation that retrieves characters from the official script tool
     * @param characterMapper mapper for converting between character creation requests and character creation
     *         DTOs
     * @param characterService service for managing characters in the system
     * @param characterUpdateMapper mapper for converting between character business objects and character
     *         update DTOs
     */
    public OfficialToolController(OfficialCharactersGateway officialCharactersGateway,
                                  Mapper<CharacterCreationRequest, CharacterCreationDTO> characterMapper,
                                  CharacterService characterService,
                                  Mapper<Character, CharacterDTO> characterUpdateMapper) {
        this.officialCharactersGateway = officialCharactersGateway;
        this.characterMapper = characterMapper;
        this.characterService = characterService;
        this.characterUpdateMapper = characterUpdateMapper;
    }

    /**
     * Retrieves the full list of characters as they currently exist in the script tool database.
     *
     * @return a REST response entity containing all characters of the script tool
     */
    @Operation(
            summary = "Retrieves all characters",
            description = "Retrieves a list of all characters that can be fetched from the official script tool. The "
                    + "characters are in the format for creating a new character in the system (basically, a "
                    + "recommendation to add them to the system)."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieval successful. The response body contains the list of characters",
                    content = {
                            @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation =
                                                            CharacterCreationDTO.class
                                            )
                                    )
                            )}
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
            ), @ApiResponse(
                    responseCode = "502",
                    description = "Bad gateway. There was an error retrieving the characters from the official script "
                            + "tool. Please try again later. If the issue persists, contact the system "
                            + "administrator/developers.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @GetMapping("/characters")
    public ResponseEntity<Object> getCharacters() {
        List<CharacterCreationDTO> transferObjects;
        try {
            transferObjects =
                    officialCharactersGateway.getAllOfficialCharacters().stream().map(characterMapper::toTransferObject)
                            .toList();
        } catch (ExternalServiceUnavailableException e) {
            return ValidationUtils.upstreamError("Failed to retrieve characters from official script tool", "/api/v1"
                    + "/officialtool/characters");
        }
        return ResponseEntity.ok(transferObjects);
    }

    /**
     * Retrieves batch update suggestions for existing characters based on the official script tool. Matches existing
     * characters with official characters by their scriptToolIdentifier and returns CharacterDTOs that can be used to
     * update the existing characters to match the official tool's current state.
     *
     * @return a REST response entity containing batch update suggestions for matching characters
     */
    @Operation(
            summary = "Retrieves batch update suggestions for existing characters",
            description = "Compares existing characters in the system with the official script tool and returns batch "
                    + "update suggestions for characters that match by scriptToolIdentifier. The returned "
                    + "CharacterDTOs "
                    + "can be used with the batch update endpoint to synchronize existing characters with the official "
                    + "tool's current state."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieval successful. The response body contains the list of update suggestions",
                    content = {
                            @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = CharacterDTO.class
                                            )
                                    )
                            )}
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
            ), @ApiResponse(
                    responseCode = "502",
                    description = "Bad gateway. There was an error retrieving the characters from the official script "
                            + "tool. Please try again later. If the issue persists, contact the system "
                            + "administrator/developers.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @GetMapping("/characters/update-suggestions")
    public ResponseEntity<Object> getUpdateSuggestions() {
        List<CharacterDTO> updateSuggestions;
        List<CharacterCreationRequest> officialCharacters;
        try {
            officialCharacters = officialCharactersGateway.getAllOfficialCharacters();
        } catch (ExternalServiceUnavailableException e) {
            return ValidationUtils.upstreamError("Failed to retrieve characters from official script tool",
                    "/api/v1/officialtool/characters/update-suggestions");
        }
        List<Character> existingCharacters = characterService.getAllCharacters();

        Map<String, Character> existingByIdentifier = existingCharacters.stream()
                .filter(c -> c.getScriptToolIdentifier() != null && !c.getScriptToolIdentifier().isBlank())
                .collect(Collectors.toMap(
                        Character::getScriptToolIdentifier,
                        c -> c,
                        (c1, c2) -> c1 // Keep first if duplicates (should not happen)
                ));

        updateSuggestions = officialCharacters.stream()
                .filter(official -> official.scriptToolIdentifier() != null
                        && !official.scriptToolIdentifier().isBlank())
                .map(official -> {
                    Character existing = existingByIdentifier.get(official.scriptToolIdentifier());
                    if (existing == null) {
                        return null; // No match found, skip
                    }

                    Character updatedCharacter = new Character(
                            existing.getId(),
                            existing.getVersion(),
                            official.scriptToolIdentifier(),
                            official.name(),
                            official.characterType(),
                            official.wikiPageLink(),
                            official.imageUrl()
                    );

                    if (updatedCharacter.equals(existing)) {
                        return null; // No changes, skip
                    }

                    return characterUpdateMapper.toTransferObject(updatedCharacter);
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(updateSuggestions);
    }
}
