package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.PlayerStatsDTO;
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

/**
 * REST controller providing different statistics about the playing group.
 */
@RestController
@RequestMapping("/api/v1/stats")
@Tag(
        name = "Statistics",
        description = "Retrieve statistics about the playing group (like leaderboards)"
)
public class StatController {
    /**
     * Retrieves statistics about each player, such as won games or amount of times the player played as part of the
     * evil team.
     *
     * @return a REST response entity containing a list of player statistics
     */
    @Operation(
            summary = "Retrieves player statistics",
            description = "Retrieves a list of statistics, each entry holding data about a single player."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieval successful. The response body contains the list of player statistics.",
                    content = {
                            @Content(
                                    array = @ArraySchema(
                                            schema =
                                            @Schema(implementation = PlayerStatsDTO.class)
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
            )}
    )
    @GetMapping("/players")
    public ResponseEntity<Object> getAllPlayerStatistics() {
        return ResponseEntity.internalServerError()
                .body("This endpoint is not yet implemented. Please check back later.");
    }
}
