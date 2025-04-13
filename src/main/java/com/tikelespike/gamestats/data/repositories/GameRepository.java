package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.GameEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository for game entities. Stores and retrieves games from the database.
 */
public interface GameRepository extends Repository<GameEntity, Long> {

    /**
     * Saves a game entity to the database.
     *
     * @param game the game entity to save
     *
     * @return the saved game entity
     */
    GameEntity save(GameEntity game);

    /**
     * Retrieves a game entity by its id.
     *
     * @param id the id of the game to find
     *
     * @return the game entity with the given id, or null if no such entity exists
     */
    GameEntity findById(Long id);

    /**
     * Retrieves all game entities from the database.
     *
     * @return the list of game entities present in the database
     */
    List<GameEntity> findAll();

    /**
     * Deletes a game entity by its id.
     *
     * @param id the id of the game to delete
     */
    void deleteById(Long id);
}
