package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.GameCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.businesslogic.mapper.UserPlayerEntityMapper;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.AlignmentEntity;
import com.tikelespike.gamestats.data.entities.GameEntity;
import com.tikelespike.gamestats.data.entities.PlayerParticipationEntity;
import com.tikelespike.gamestats.data.entities.ScriptEntity;
import com.tikelespike.gamestats.data.repositories.CharacterRepository;
import com.tikelespike.gamestats.data.repositories.GameRepository;
import com.tikelespike.gamestats.data.repositories.PlayerRepository;
import com.tikelespike.gamestats.data.repositories.ScriptRepository;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service class for managing single games of Blood on the Clocktower.
 */
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final ScriptRepository scriptRepository;
    private final Mapper<Game, GameEntity> gameMapper;
    private final Mapper<PlayerParticipation, PlayerParticipationEntity> playerParticipationMapper;
    private final Mapper<Alignment, AlignmentEntity> alignmentMapper;
    private final UserPlayerEntityMapper playerMapper;
    private final CharacterRepository characterRepository;
    private final PlayerRepository playerRepository;

    /**
     * Creates a new game service. This is usually done by the Spring framework, which manages the service's lifecycle
     * and injects the required dependencies.
     *
     * @param gameRepository repository managing game entities in the database
     * @param scriptRepository repository managing script entities in the database
     * @param gameMapper mapper for converting between game business objects and game entities
     * @param playerParticipationMapper mapper for converting between player participation business objects and
     *         their database representation
     * @param alignmentMapper mapper for converting between alignment business objects and their database
     *         representation
     * @param playerMapper mapper for converting between player business objects and their database
     *         representation
     * @param characterRepository repository managing character entities in the database
     * @param playerRepository repository managing player entities in the database
     */
    public GameService(GameRepository gameRepository, ScriptRepository scriptRepository,
                       Mapper<Game, GameEntity> gameMapper,
                       Mapper<PlayerParticipation, PlayerParticipationEntity> playerParticipationMapper,
                       Mapper<Alignment, AlignmentEntity> alignmentMapper, UserPlayerEntityMapper playerMapper,
                       CharacterRepository characterRepository,
                       PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.scriptRepository = scriptRepository;
        this.gameMapper = gameMapper;
        this.playerParticipationMapper = playerParticipationMapper;
        this.alignmentMapper = alignmentMapper;
        this.playerMapper = playerMapper;
        this.characterRepository = characterRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Creates a new game in the system.
     *
     * @param request the request containing the information needed to create the game. May not be null.
     *
     * @return the created game
     * @throws RelatedResourceNotFoundException if referenced resources (script, characters, players) do not
     *         exist
     */
    @Transactional
    public Game createGame(GameCreationRequest request) {
        Objects.requireNonNull(request, "Request may not be null");

        ScriptEntity scriptEntity = scriptRepository.findById(request.script().getId());
        if (scriptEntity == null) {
            throw new RelatedResourceNotFoundException(
                    "Script with id " + request.script().getId() + " does not exist");
        }

        for (PlayerParticipation participation : request.participants()) {
            verifyParticipationResourcesExist(participation);
        }

        // Verify storytellers exist
        for (Player storyteller : request.storytellers()) {
            if (storyteller != null && playerRepository.findById(storyteller.getId()) == null) {
                throw new RelatedResourceNotFoundException(
                        "Storyteller with id " + storyteller.getId() + " does not exist");
            }
        }

        GameEntity savedEntity = gameRepository.save(new GameEntity(
                null,
                null,
                scriptEntity,
                alignmentMapper.toTransferObject(request.winningAlignment()),
                request.description(),
                request.participants().stream().map(playerParticipationMapper::toTransferObject).toList(),
                request.winningAlignment() != null ? null
                        : request.winningPlayers().stream().map(playerMapper::toTransferObject).toList(),
                request.name(),
                request.storytellers().stream().map(playerMapper::toTransferObject).toList()
        ));
        return gameMapper.toBusinessObject(savedEntity);
    }

    private void verifyParticipationResourcesExist(PlayerParticipation participation) {
        if (participation.getInitialCharacter() != null
                && characterRepository.findById(participation.getInitialCharacter().getId()) == null) {
            throw new RelatedResourceNotFoundException(
                    "Character with id " + participation.getInitialCharacter().getId() + " does not exist");
        }

        if (participation.getEndCharacter() != null
                && characterRepository.findById(participation.getEndCharacter().getId()) == null) {
            throw new RelatedResourceNotFoundException(
                    "Character with id " + participation.getEndCharacter().getId() + " does not exist");
        }

        if (participation.getPlayer() != null && playerRepository.findById(participation.getPlayer().getId()) == null) {
            throw new RelatedResourceNotFoundException(
                    "Player with id " + participation.getPlayer().getId() + " does not exist");
        }
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param id the ID of the game to retrieve
     *
     * @return the game with the given ID, or null if no such game exists
     */
    public Game getGame(long id) {
        GameEntity gameEntity = gameRepository.findById(id);
        return gameMapper.toBusinessObject(gameEntity);
    }

    /**
     * Retrieves all games in the system.
     *
     * @return a list of all games
     */
    public List<Game> getAllGames() {
        List<GameEntity> gameEntities = gameRepository.findAll();
        return gameEntities.stream()
                .map(gameMapper::toBusinessObject)
                .toList();
    }

    /**
     * Updates an existing game in the system.
     *
     * @param game the game to update. A game with the same id must already exist in the system.
     *
     * @return the updated game
     * @throws ResourceNotFoundException if the game with the given id does not exist
     * @throws StaleDataException if the game has been modified or deleted in the meantime (concurrently)
     */
    @Transactional(rollbackFor = {StaleDataException.class})
    public Game updateGame(Game game) throws StaleDataException {
        Objects.requireNonNull(game, "Game may not be null");

        if (gameRepository.findById(game.getId()) == null) {
            throw new ResourceNotFoundException("Game with id " + game.getId() + " does not exist");
        }

        ScriptEntity scriptEntity = scriptRepository.findById(game.getScript().getId());
        if (scriptEntity == null) {
            throw new RelatedResourceNotFoundException(
                    "Script with id " + game.getScript().getId() + " does not exist");
        }

        for (PlayerParticipation participation : game.getParticipants()) {
            verifyParticipationResourcesExist(participation);
        }

        GameEntity entityToSave = gameMapper.toTransferObject(game);
        GameEntity savedEntity;
        try {
            savedEntity = gameRepository.save(entityToSave);
        } catch (StaleObjectStateException | OptimisticLockException | ObjectOptimisticLockingFailureException e) {
            throw new StaleDataException(e);
        }
        return gameMapper.toBusinessObject(savedEntity);
    }

    /**
     * Removes a game from the system. No effect if the game does not exist.
     *
     * @param id the ID of the game to delete
     */
    public void deleteGame(long id) {
        gameRepository.deleteById(id);
    }
}
