package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.PlayerEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository for player entities. Stores and retrieves player data from the database.
 */
public interface PlayerRepository extends Repository<PlayerEntity, Long> {
    /**
     * Saves the given player entity to the database.
     *
     * @param playerEntity the player to save
     *
     * @return the player as it was saved
     */
    PlayerEntity save(PlayerEntity playerEntity);

    /**
     * Retrieves the player entity with the given id from the database.
     *
     * @param id unique numerical identifier
     *
     * @return the player entity with the given id, or null if no such player exists
     */
    PlayerEntity findById(long id);

    /**
     * Retrieves all player entities in the database.
     *
     * @return all player entities in the database
     */
    List<PlayerEntity> findAll();

    /**
     * Checks if a player with the given id exists in the system.
     *
     * @param id the unique identifier of the player
     *
     * @return true if a player with the given id exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Deletes the player entity with the given id from the database.
     *
     * @param id the unique identifier of the player
     */
    void deleteById(Long id);
}
