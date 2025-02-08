package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.PlayerCreationDTO;
import com.tikelespike.gamestats.api.entities.PlayerDTO;
import com.tikelespike.gamestats.api.mapper.PlayerMapper;
import com.tikelespike.gamestats.api.validation.ValidationUtils;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.services.PlayerService;
import com.tikelespike.gamestats.businesslogic.services.UserService;
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
 * REST controller for handling players. A player is a participant in a game, and can be associated with a user. There
 * are also players that do not have a user account in this application. Players can be created, read, updated, and
 * deleted through this controller.
 */
@RestController
@RequestMapping("/api/v1/players")
@Tag(
        name = "Player Management",
        description = "Operations for managing players"
)
public class PlayerController {

    private final PlayerService playerService;
    private final UserService userService;
    private final PlayerMapper playerMapper;

    /**
     * Creates a new player controller. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param playerService business logic service for managing players
     * @param userService business logic service for managing users
     * @param playerMapper mapper for converting between player data transfer objects and business objects
     */
    public PlayerController(PlayerService playerService, UserService userService, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.userService = userService;
        this.playerMapper = playerMapper;
    }

    /**
     * Retrieves a list of all players in the system.
     *
     * @return a REST response entity containing the list of players registered in the system
     */
    @Operation(
            summary = "Retrieves all players",
            description = "Retrieves a list of all players in the system."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "List of players retrieved successfully.",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PlayerDTO.class)))}
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Please try again later. If the issue persists, contact "
                            + "the system administrator or development team.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @GetMapping()
    public ResponseEntity<List<PlayerDTO>> getPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayers().stream().map(playerMapper::toTransferObject).toList();
        return ResponseEntity.ok(players);
    }

    /**
     * Creates a new player in the system. The player can be associated with a user account or be a standalone player.
     *
     * @param player the player data transfer object containing the data about the new player
     *
     * @return a REST response entity containing the newly created player
     */
    @Operation(
            summary = "Creates a new player",
            description =
                    "Creates a new player in the system. The player can be associated with a user account or be a "
                            + "standalone player, depending on whether an owner ID is provided. If no owner ID is "
                            + "provided, you must provide a name for the player. If you create a player associated "
                            + "with a user account, that user's name will override the player's name."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "201",
                    description = "Created player successfully. The response body contains the newly created player.",
                    content = {@Content(schema = @Schema(implementation = PlayerDTO.class))}
            ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request. The response body contains an error message.",
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
    public ResponseEntity<Object> createPlayer(@RequestBody PlayerCreationDTO player) {
        if (player.ownerId() != null) {
            return createAssociatedPlayer(player);
        }
        if (player.name() != null && !player.name().isBlank()) {
            return createUnassociatedPlayer(player);
        }
        return ValidationUtils.requestInvalid("Either an owner ID or a name must be provided", "/api/v1/players");
    }

    private ResponseEntity<Object> createUnassociatedPlayer(PlayerCreationDTO player) {
        PlayerDTO createdPlayer = playerMapper.toTransferObject(playerService.createPlayer(player.name()));
        return ResponseEntity.created(URI.create("/api/v1/players/" + createdPlayer.id())).body(createdPlayer);
    }

    private ResponseEntity<Object> createAssociatedPlayer(PlayerCreationDTO player) {
        User owner = userService.loadUser(player.ownerId());
        if (owner == null) {
            return ValidationUtils.requestInvalid("Owner ID " + player.ownerId() + " does not exist",
                    "/api/v1/players");
        }
        if (owner.getPlayer() != null) {
            return ValidationUtils.requestInvalid("User " + player.ownerId() + " already has a player",
                    "/api/v1/players");
        }
        // Theoretically, this is not thread-safe, because the user could have been e.g. deleted since we loaded it
        // above. For the sake of this small application, we ignore this because it is unlikely to happen with the
        // expected size of the userbase and the number of concurrent requests. Also, in the worst case scenario, the
        // request will simply fail with a 500 error, which is acceptable for this application.
        PlayerDTO createdPlayer = playerMapper.toTransferObject(playerService.createPlayer(owner));
        return ResponseEntity.created(URI.create("/api/v1/players/" + createdPlayer.id())).body(createdPlayer);
    }

    /**
     * Retrieves a player by its unique identifier.
     *
     * @param id the unique identifier of the player
     *
     * @return a REST response entity containing the player with the given ID
     */
    @Operation(
            summary = "Retrieves a player by ID",
            description =
                    "Retrieves a player by its unique identifier. If no player with the given ID exists, a 404 Not "
                            + "Found response is returned."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieved player successfully. The response body contains the player.",
                    content = {@Content(schema = @Schema(implementation = PlayerDTO.class))}
            ), @ApiResponse(
                    responseCode = "404",
                    description = "The player with the requested id does not exist.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Please try again later. If the issue persists, contact "
                            + "the system administrator or development team.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPlayer(@PathVariable("id") long id) {
        Player player = playerService.getPlayerById(id);
        if (player == null) {
            return ValidationUtils.notFound("/api/v1/players/" + id);
        }
        return ResponseEntity.ok(playerMapper.toTransferObject(player));
    }

    /**
     * Updates a player in the system. The player must already exist in the system.
     *
     * @param id the unique identifier of the player
     * @param player the player data transfer object containing the updated data
     *
     * @return a REST response entity containing the updated player
     */
    @Operation(
            summary = "Updates a player",
            description =
                    "Updates a player in the system. The player must already exist in the system. The player ID in the "
                            + "path and in the request body must match. If the player is associated with a user, the "
                            + "user's name will override the player's name (updating the name directly will not have "
                            + "any effect. If the player is associated with a user, the owner ID must be provided. If"
                            + " the player is not associated with a user, the name must be provided. You can change "
                            + "whether the player is associated with a user by providing or removing the owner ID."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Updated player successfully. The response body contains the updated player.",
                    content = {@Content(schema = @Schema(implementation = PlayerDTO.class))}
            ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request. The response body contains an error message.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "404",
                    description = "The player with the requested id does not exist. Create it first by POSTing to "
                            + "/api/v1/players.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Please try again later. If the issue persists, contact "
                            + "the system administrator or development team.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STORYTELLER')")
    public ResponseEntity<Object> updatePlayer(@PathVariable("id") long id, @RequestBody PlayerDTO player) {
        if (!playerService.playerExists(id)) {
            return ValidationUtils.notFound("/api/v1/players/" + id);
        }

        if (player.id() != id) {
            return ValidationUtils.requestInvalid("Player ID in path and body do not match (id cannot be changed)",
                    "/api/v1/players/" + id);
        }
        if ((player.name() == null || player.name().isBlank()) && player.ownerId() == null) {
            return ValidationUtils.requestInvalid("Either a name or an owner ID must be provided",
                    "/api/v1/players/" + id);
        }
        if (player.ownerId() != null && !userService.userExists(player.ownerId())) {
            return ValidationUtils.requestInvalid("Owner ID " + player.ownerId() + " does not exist",
                    "/api/v1/players/" + id);
        }
        Player updatedPlayer = playerService.updatePlayer(playerMapper.toBusinessObject(player));
        return ResponseEntity.ok(playerMapper.toTransferObject(updatedPlayer));
    }

    /**
     * Deletes a player from the system.
     *
     * @param id the unique identifier of the player
     *
     * @return a REST response entity indicating the success of the operation
     */
    @Operation(
            summary = "Deletes a player",
            description = "Deletes a player from the system. If the player does not exist, a 404 Not Found response is "
                    + "returned."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Deleted player successfully."
            ), @ApiResponse(
                    responseCode = "404",
                    description = "The player with the requested id does not exist.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            ), @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. Please try again later. If the issue persists, contact "
                            + "the system administrator or development team.",
                    content = {@Content(schema = @Schema(implementation = ErrorEntity.class))}
            )}
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STORYTELLER')")
    public ResponseEntity<Object> deletePlayer(@PathVariable("id") long id) {
        if (!playerService.playerExists(id)) {
            return ValidationUtils.notFound("/api/v1/players/" + id);
        }
        playerService.deletePlayer(id);
        return ResponseEntity.ok().build();
    }

}
