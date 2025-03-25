package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.CharacterDTO;
import com.tikelespike.gamestats.api.entities.ErrorEntity;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for accessing the official script tool and provided by it/scraped from it.
 */
@RestController
@RequestMapping("/api/v1/officialtool")
@Tag(
        name = "Official Script Tool API",
        description = "Access information provided by the official script tool."
)
public class OfficialToolController {
    /**
     * Retrieves the full list of characters as they currently exist in the script tool database.
     *
     * @return a REST response entity containing all characters of the script tool
     */
    @Operation(
            summary = "Retrieves all characters",
            description = "Retrieves a list of all characters that can be fetched from the official script tool"
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
        List<CharacterDTO> transferObjects = new ArrayList<>();
        return ResponseEntity.ok(transferObjects);
    }
}
