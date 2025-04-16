package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.ErrorEntity;
import com.tikelespike.gamestats.api.entities.GameCreationRequestDTO;
import com.tikelespike.gamestats.api.entities.GameDTO;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.ValidationUtils;
import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.GameCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.businesslogic.services.GameService;
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
 * REST controller for managing games. A game represents a single playthrough of Blood on the Clocktower with a specific
 * set of players, characters, and outcomes.
 */
@RestController
@RequestMapping("/api/v1/games")
@Tag(
        name = "Game Management",
        description = "Operations for managing games of Blood on the Clocktower"
)
public class GameController {
    private static final String API_PATH = "/api/v1/games";
    private static final String API_PATH_WITH_SUBPATH = API_PATH + "/";
    private final GameService gameService;
    private final Mapper<Game, GameDTO> gameMapper;
    private final Mapper<GameCreationRequest, GameCreationRequestDTO> creationMapper;

    /**
     * Creates a new GameController. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param gameService the business layer game service to use for managing games. May not be null.
     * @param gameMapper maps between game business objects and their REST representations
     * @param creationMapper maps between game creation requests and their REST representations
     */
    public GameController(GameService gameService, Mapper<Game, GameDTO> gameMapper,
                          Mapper<GameCreationRequest, GameCreationRequestDTO> creationMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.creationMapper = creationMapper;
    }

    /**
     * Creates a new game.
     *
     * @param creationRequest REST dto containing the data
     *
     * @return a REST response entity containing the new game
     */
    @Operation(
            summary = "Creates a new game",
            description = "Creates a new game of Blood on the Clocktower with the specified players, characters, and "
                    + "outcomes."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "201",
                    description = "Created game successfully. The response body contains the newly created game.",
                    content = {@Content(schema = @Schema(implementation = GameDTO.class))}
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
    public ResponseEntity<Object> createGame(@RequestBody GameCreationRequestDTO creationRequest) {
        ValidationResult validation = creationRequest.validate();
        if (!validation.isValid()) {
            return ValidationUtils.requestInvalid(validation.getMessage(), API_PATH);
        }

        Game game;
        try {
            game = gameService.createGame(creationMapper.toBusinessObject(creationRequest));
        } catch (RelatedResourceNotFoundException e) {
            return ValidationUtils.requestInvalid(
                    "At least one of the referenced resources (script, characters, players) does not exist",
                    API_PATH
            );
        }

        GameDTO transferObject = gameMapper.toTransferObject(game);
        URI gameURI = URI.create(API_PATH_WITH_SUBPATH + game.getId());
        return ResponseEntity.created(gameURI).body(transferObject);
    }

    /**
     * Retrieves all games.
     *
     * @return a REST response entity containing all games currently known to the system
     */
    @Operation(
            summary = "Retrieves all games",
            description = "Retrieves a list of all games registered in the system."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Retrieval successful. The response body contains the list of games",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = GameDTO.class)))}
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
    public ResponseEntity<Object> getGames() {
        List<Game> games = gameService.getAllGames();

        List<GameDTO> transferObjects = games.stream().map(gameMapper::toTransferObject).toList();
        return ResponseEntity.ok(transferObjects);
    }

    /**
     * Updates a game.
     *
     * @param id game id (must be the same as in the body, if given there)
     * @param gameDTO game to update
     *
     * @return a REST response entity containing the updated game
     */
    @Operation(
            summary = "Updates a game",
            description = "Updates the details about an existing game, like the players, characters, or outcomes. The "
                    + "game has to be created before it can be changed by this endpoint."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Updated the game successfully. The response body contains the updated game.",
                    content = {@Content(schema = @Schema(implementation = GameDTO.class))}
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
    public ResponseEntity<Object> updateGame(@PathVariable("id") long id, @RequestBody GameDTO gameDTO) {
        ValidationResult validation = gameDTO.validateUpdate(id);
        if (!validation.isValid()) {
            return ValidationUtils.requestInvalid(validation.getMessage(), API_PATH_WITH_SUBPATH + id);
        }

        Game gameUpdate;
        try {
            gameUpdate = gameMapper.toBusinessObject(gameDTO);
        } catch (RelatedResourceNotFoundException e) {
            return ValidationUtils.requestInvalid(
                    "At least one of the referenced resources (script, characters, players) does not exist",
                    API_PATH_WITH_SUBPATH + id
            );
        }

        Game updatedGame;
        try {
            updatedGame = gameService.updateGame(gameUpdate);
        } catch (ResourceNotFoundException e) {
            return ValidationUtils.notFound(API_PATH_WITH_SUBPATH + id);
        } catch (RelatedResourceNotFoundException e) {
            return ValidationUtils.requestInvalid("At least one of the referenced resources (script, characters, "
                    + "players) does not exist", API_PATH_WITH_SUBPATH + id);
        } catch (StaleDataException e) {
            return ValidationUtils.conflict(API_PATH_WITH_SUBPATH + id);
        }

        GameDTO transferObject = gameMapper.toTransferObject(updatedGame);
        return ResponseEntity.ok(transferObject);
    }

    /**
     * Deletes a game from the system.
     *
     * @param id the ID of the game to delete
     *
     * @return a REST response entity indicating the result of the operation
     */
    @Operation(
            summary = "Deletes a game",
            description = "Removes a game from the system. If the game does not exist, the operation has no effect."
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "204",
                    description = "Game deleted successfully. No content is returned."
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
    public ResponseEntity<Object> deleteGame(@PathVariable("id") long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
