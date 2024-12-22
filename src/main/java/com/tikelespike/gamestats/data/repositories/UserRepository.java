package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

}
