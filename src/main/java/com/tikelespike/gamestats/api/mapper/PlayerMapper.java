package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.PlayerDTO;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.services.UserService;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between the player business object and the player data transfer object used in the REST interface.
 */
@Component
public class PlayerMapper extends Mapper<Player, PlayerDTO> {

    private final UserService userService;

    /**
     * Creates a new player mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle
     * and injects the required dependencies.
     *
     * @param userService the user service to use for loading user details
     */
    public PlayerMapper(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Player toBusinessObjectNoCheck(PlayerDTO transferObject) {
        return new Player(
                transferObject.id(),
                transferObject.name(),
                transferObject.ownerId() == null ? null : userService.loadUser(transferObject.ownerId())
        );
    }

    @Override
    public PlayerDTO toTransferObjectNoCheck(Player businessObject) {
        return new PlayerDTO(
                businessObject.getId(),
                businessObject.getName(),
                businessObject.getOwner() == null ? null : businessObject.getOwner().getId()
        );
    }
}
