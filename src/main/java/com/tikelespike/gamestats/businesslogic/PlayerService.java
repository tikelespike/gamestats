package com.tikelespike.gamestats.businesslogic;

import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.mapper.UserPlayerEntityMapper;
import com.tikelespike.gamestats.data.repositories.PlayerRepository;
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

    /**
     * Creates a new player service. This is usually done by the Spring framework, which manages the service's lifecycle
     * and injects the required dependencies.
     *
     * @param playerRepository repository managing player entities in the database
     * @param mapper mapper for converting between player business objects and player entities
     */
    public PlayerService(PlayerRepository playerRepository, UserPlayerEntityMapper mapper) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
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
        if (owner.getPlayer() != null) {
            throw new IllegalStateException("User already has a player");
        }
        Player player = new Player(owner);
        owner.setPlayer(player);
        return mapper.toBusinessObject(playerRepository.save(mapper.toTransferObject(player)));
    }
}
