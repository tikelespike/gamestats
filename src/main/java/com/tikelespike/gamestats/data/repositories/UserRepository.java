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
}
