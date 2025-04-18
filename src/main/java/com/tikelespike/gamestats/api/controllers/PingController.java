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

/**
 * REST controller for a simple ping/health check endpoint.
 */
@RestController
@RequestMapping("/api/v1/ping")
@Tag(
        name = "Ping",
        description = "Simple ping endpoint for health checks"
)
public class PingController {

    /**
     * Returns a simple ping response. This is used for health checks and to verify that the server is running.
     *
     * @return a REST response entity with a simple ping message
     */
    @Operation(
            summary = "Pings the service",
            description = "Returns a simple ping response. This is used for health checks and to verify that the "
                    + "server is running."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Ping successful. The response body contains a simple hello message.",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CharacterDTO.class)))}
            ), @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. Your session has expired or you are not logged in. Please sign in "
                            + "again.",
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
        return ResponseEntity.ok("Pong!");
    }
}
