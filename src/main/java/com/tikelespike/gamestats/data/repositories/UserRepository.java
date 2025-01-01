package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for user entities. Stores and retrieves user data from the database.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Retrieves the user entity with the given email address.
     *
     * @param email user email address
     *
     * @return the user entity with the given email address
     */
    UserEntity findByEmail(String email);

    /**
     * Retrieves the user entity with the given id from the database.
     *
     * @param id the unique identifier of the user
     *
     * @return the user with the given id, or null if no such user exists
     */
    UserEntity findById(long id);
}
