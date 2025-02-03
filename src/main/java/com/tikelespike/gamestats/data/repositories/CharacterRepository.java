package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.CharacterEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository for character entities. Stores and retrieves character data from the database.
 */
public interface CharacterRepository extends Repository<CharacterEntity, Long> {
    /**
     * Saves a character entity to the database.
     *
     * @param character the character entity to save
     *
     * @return the saved character entity
     */
    CharacterEntity save(CharacterEntity character);

    /**
     * Retrieves all character entities from the database.
     *
     * @return the list of character entities present in the database
     */
    List<CharacterEntity> findAll();

    /**
     * Retrieves a character entity by its id.
     *
     * @param id the id of the character to find
     *
     * @return the character entity with the given id, or null if no such entity exists
     */
    CharacterEntity findById(Long id);

    /**
     * Deletes a character entity by its id.
     *
     * @param id the id of the character to delete
     */
    void deleteById(Long id);
}
