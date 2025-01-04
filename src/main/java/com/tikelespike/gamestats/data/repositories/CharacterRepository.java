package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.CharacterEntity;
import org.springframework.data.repository.Repository;

/**
 * Repository for character entities. Stores and retrieves character data from the database.
 */
public interface CharacterRepository extends Repository<CharacterEntity, Long> {
}
