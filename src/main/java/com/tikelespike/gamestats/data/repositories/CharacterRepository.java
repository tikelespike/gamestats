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
}
