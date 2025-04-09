package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.CharacterEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

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
     * Saves multiple character entities to the database in a single operation.
     *
     * @param characters the character entities to save
     *
     * @return the list of saved character entities
     */
    Iterable<CharacterEntity> saveAll(Iterable<CharacterEntity> characters);

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
     * Retrieves exactly those characters the ids of which are in the passed list of ids. Acquires a pessimistic write
     * lock, that is, the characters cannot be changed until the lock is released.
     *
     * @param ids ids of the characters to fetch
     *
     * @return the list of characters with those ids
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM characters c WHERE c.id IN :ids")
    List<CharacterEntity> findAllByIdWithLock(@Param("ids") List<Long> ids);

    /**
     * Deletes a character entity by its id.
     *
     * @param id the id of the character to delete
     */
    void deleteById(Long id);
}
