package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.ScriptEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository for script entities. Stores and retrieves game scripts from the database.
 */
public interface ScriptRepository extends Repository<ScriptEntity, Long> {

    /**
     * Saves a script entity to the database.
     *
     * @param script the script entity to save
     *
     * @return the saved script entity
     */
    ScriptEntity save(ScriptEntity script);

    /**
     * Retrieves a script entity by its id.
     *
     * @param id the id of the script to find
     *
     * @return the script entity with the given id, or null if no such entity exists
     */
    ScriptEntity findById(Long id);

    /**
     * Retrieves all script entities that contain all of the given characters.
     *
     * @param characters characters the returned scripts should contain
     *
     * @return a list of those scripts that contain the given characters
     */
    List<ScriptEntity> findAllByCharactersContains(List<CharacterEntity> characters);

    /**
     * Retrieves all script entities from the database.
     *
     * @return the list of script entities present in the database
     */
    List<ScriptEntity> findAll();

    /**
     * Deletes a script entity by its id.
     *
     * @param id the id of the script to delete
     */
    void deleteById(Long id);
}
