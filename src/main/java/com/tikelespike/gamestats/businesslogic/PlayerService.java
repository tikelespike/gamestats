package com.tikelespike.gamestats.businesslogic;

import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.mapper.UserPlayerEntityMapper;
import com.tikelespike.gamestats.data.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserPlayerEntityMapper mapper;

    public PlayerService(PlayerRepository playerRepository, UserPlayerEntityMapper mapper) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }

    public Player createPlayer(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        Player player = new Player(name);
        return mapper.toBusinessObject(playerRepository.save(mapper.toTransferObject(player)));
    }

    public Player createPlayer(User owner) {
        if (owner.getPlayer() != null) {
            throw new IllegalStateException("User already has a player");
        }
        Player player = new Player(owner);
        owner.setPlayer(player);
        return mapper.toBusinessObject(playerRepository.save(mapper.toTransferObject(player)));
    }
}
