package com.tikelespike.gamestats.businesslogic;

import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.mapper.UserPlayerEntityMapper;
import com.tikelespike.gamestats.data.entities.UserEntity;
import com.tikelespike.gamestats.data.repositories.PlayerRepository;
import com.tikelespike.gamestats.data.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing players. A player is a participant in games and can be associated with a user. There can also be
 * players that do not have a user account in this application.
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserPlayerEntityMapper mapper;
    private final UserRepository userRepository;

    /**
     * Creates a new player service. This is usually done by the Spring framework, which manages the service's lifecycle
     * and injects the required dependencies.
     *
     * @param playerRepository repository managing player entities in the database
     * @param mapper mapper for converting between player business objects and player entities
     * @param userRepository repository managing user accounts
     */
    public PlayerService(PlayerRepository playerRepository, UserPlayerEntityMapper mapper,
                         UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    /**
     * Returns all players in the system. This includes players that are associated with users and players that are
     * not.
     *
     * @return a list of all players in the system
     */
    public List<Player> getAllPlayers() {
        return playerRepository.findAll().stream().map(mapper::toBusinessObject).toList();
    }

    /**
     * Retrieves a player by its unique identifier.
     *
     * @param id the unique identifier of the player
     *
     * @return the player with the given id, or null if no such player exists
     */
    public Player getPlayerById(long id) {
        return mapper.toBusinessObject(playerRepository.findById(id));
    }

    /**
     * Checks if a player with the given id exists in the system.
     *
     * @param id the unique identifier of the player
     *
     * @return true if a player with the given id exists, false otherwise
     */
    public boolean playerExists(Long id) {
        return playerRepository.existsById(id);
    }

    /**
     * Creates a new player in the system with the given name. The player is not associated with any user.
     *
     * @param name human-readable name of the player (usually the real-world name of the person). Must not be
     *         null or empty.
     *
     * @return the newly created player
     */
    public Player createPlayer(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        Player player = new Player(name);
        return mapper.toBusinessObject(playerRepository.save(mapper.toTransferObject(player)));
    }

    /**
     * Creates a new player in the system that is controlled by the given user. The owning user and the player usually
     * represent the same real-world person who both uses the application and participates in games.
     *
     * @param owner the user that manages the player. Must not be null and must not already have a different
     *         player associated with it.
     *
     * @return the newly created player
     */
    public Player createPlayer(User owner) {
        return createPlayer(owner.getId());
    }

    /**
     * Creates a new player in the system that is controlled by the given user. The owning user and the player usually
     * represent the same real-world person who both uses the application and participates in games.
     *
     * @param ownerId the id of user that manages the player. Must exist in the system and must not already have
     *         a different player associated with it.
     */
    public Player createPlayer(long ownerId) {
        UserEntity userEntity = userRepository.findById(ownerId);
        if (userEntity == null) {
            throw new IllegalArgumentException("User does not exist");
        }
        User owner = mapper.toBusinessObject(userEntity);
        if (owner.getPlayer() != null) {
            throw new IllegalStateException("User already has a player");
        }
        Player player = new Player(owner);
        owner.setPlayer(player);
        return mapper.toBusinessObject(playerRepository.save(mapper.toTransferObject(player)));
    }

    /**
     * Updates the player in the system. The player must already exist in the system.
     *
     * @param player the updated player data
     *
     * @return the updated player
     */
    public Player updatePlayer(Player player) {
        if (player.getId() == null) {
            throw new IllegalArgumentException("Player must have an id");
        }
        if (!playerRepository.existsById(player.getId())) {
            throw new IllegalArgumentException("Player does not exist");
        }
        if ((player.getName() == null || player.getName().isBlank()) && player.getOwner() == null) {
            throw new IllegalArgumentException("Player must have a name or an owner");
        }
        return mapper.toBusinessObject(playerRepository.save(mapper.toTransferObject(player)));
    }

    /**
     * Deletes a player from the system. The player must already exist in the system.
     *
     * @param id the unique identifier of the player to delete
     */
    public void deletePlayer(Long id) {
        Player player = getPlayerById(id);
        if (player == null) {
            throw new IllegalArgumentException("Player does not exist");
        }

        User owner = player.getOwner();
        if (owner != null) {
            owner.setPlayer(null);
            userRepository.save(mapper.toTransferObject(owner));
        }

        playerRepository.deleteById(id);
    }
}
