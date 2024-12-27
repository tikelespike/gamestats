package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.PlayerDTO;
import com.tikelespike.gamestats.businesslogic.UserService;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper implements Mapper<Player, PlayerDTO> {

    private final UserService userService;

    public PlayerMapper(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Player toBusinessObject(PlayerDTO transferObject) {
        return new Player(
                transferObject.getId(),
                transferObject.getName(),
                userService.loadUser(transferObject.getOwnerId())
        );
    }

    @Override
    public PlayerDTO toTransferObject(Player businessObject) {
        return new PlayerDTO(
                businessObject.getId(),
                businessObject.getName(),
                businessObject.getOwner() == null ? null : businessObject.getOwner().getId()
        );
    }
}
