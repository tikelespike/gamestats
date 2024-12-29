package com.tikelespike.gamestats.api.resources;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.PlayerCreationDTO;
import com.tikelespike.gamestats.api.entities.PlayerDTO;
import com.tikelespike.gamestats.api.mapper.PlayerMapper;
import com.tikelespike.gamestats.businesslogic.PlayerService;
import com.tikelespike.gamestats.businesslogic.UserService;
import com.tikelespike.gamestats.businesslogic.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for handling players. A player is a participant in a game, and can be associated with a user. There
 * are also players that do not have a user account in this application. Players can be created, read, updated, and
 * deleted through this controller.
 */
@RestController
@RequestMapping("/api/v1/players")
public final class PlayerController {

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
            )}
    )
    @GetMapping()
    public ResponseEntity<List<PlayerDTO>> getPlayers() {
        return ResponseEntity.ok(new ArrayList<>());
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
    @PostMapping()
    public ResponseEntity<Object> createPlayer(@RequestBody PlayerCreationDTO player) {
        if (player.ownerId() != null) {
            return createAssociatedPlayer(player);
        }
        if (player.name() != null && !player.name().isBlank()) {
            return createUnassociatedPlayer(player);
        }
        return requestInvalid("Either an owner ID or a name must be provided", "/api/v1/players");
    }

    private ResponseEntity<Object> createUnassociatedPlayer(PlayerCreationDTO player) {
        PlayerDTO createdPlayer = playerMapper.toTransferObject(playerService.createPlayer(player.name()));
        return ResponseEntity.created(URI.create("/api/v1/players/" + createdPlayer.id())).body(createdPlayer);
    }

    private ResponseEntity<Object> createAssociatedPlayer(PlayerCreationDTO player) {
        User owner = userService.loadUser(player.ownerId());
        if (owner == null) {
            return requestInvalid("Owner ID " + player.ownerId() + " does not exist", "/api/v1/players");
        }
        if (owner.getPlayer() != null) {
            return requestInvalid("User " + player.ownerId() + " already has a player", "/api/v1/players");
        }
        // Theoretically, this is not thread-safe, because the user could have been e.g. deleted since we loaded it
        // above. For the sake of this small application, we ignore this because it is unlikely to happen with the
        // expected size of the userbase and the number of concurrent requests. Also, in the worst case scenario, the
        // request will simply fail with a 500 error, which is acceptable for this application.
        PlayerDTO createdPlayer = playerMapper.toTransferObject(playerService.createPlayer(owner));
        return ResponseEntity.created(URI.create("/api/v1/players/" + createdPlayer.id())).body(createdPlayer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable("id") long id) {
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable("id") long id, PlayerDTO player) {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable("id") long id) {
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Object> requestInvalid(String message, String path) {
        return ResponseEntity.badRequest().body(ErrorEntity.badRequest(message, path));
    }
}
