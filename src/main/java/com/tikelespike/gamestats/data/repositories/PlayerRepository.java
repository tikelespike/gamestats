package com.tikelespike.gamestats.data.repositories;

import com.tikelespike.gamestats.data.entities.PlayerEntity;
import org.springframework.data.repository.Repository;

public interface PlayerRepository extends Repository<PlayerEntity, Long> {
    PlayerEntity save(PlayerEntity playerEntity);

    PlayerEntity findById(long id);

}
