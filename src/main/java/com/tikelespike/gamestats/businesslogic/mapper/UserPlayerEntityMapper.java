package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.entities.UserRole;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.PlayerEntity;
import com.tikelespike.gamestats.data.entities.UserEntity;
import com.tikelespike.gamestats.data.entities.UserRoleEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Maps between the user business object and the user entity database representation, as well as between players and
 * their database representation. These two are tightly coupled, so it is necessary to convert them together.
 */
@Component
public class UserPlayerEntityMapper {

    private final Mapper<UserRole, UserRoleEntity> roleMapper;

    /**
     * Creates a new mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param roleMapper mapper for the user roles
     */
    public UserPlayerEntityMapper(UserRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * Maps a user entity database object to a user business object. This will also map the associated player object, if
     * necessary.
     *
     * @param transferObject database representation of the user
     *
     * @return a user business object equivalent to the transferObject
     */
    public User toBusinessObject(UserEntity transferObject) {
        if (transferObject == null) {
            return null;
        }

        Player partialPlayer = null;
        if (transferObject.getPlayer() != null) {
            partialPlayer = toPartialBusinessObject(transferObject.getPlayer());
        }
        User user = toPartialBusinessObject(transferObject);
        user.setPlayer(partialPlayer);
        if (partialPlayer != null) {
            partialPlayer.setOwner(user);
        }
        return user;
    }

    /**
     * Maps a user business object to a user entity database object. This will also map the associated player object, if
     * necessary.
     *
     * @param businessObject business object to map
     *
     * @return a user entity equivalent to the businessObject
     */
    public UserEntity toTransferObject(User businessObject) {
        if (businessObject == null) {
            return null;
        }

        PlayerEntity partialPlayer = null;
        if (businessObject.getPlayer() != null) {
            partialPlayer = toPartialTransferObject(businessObject.getPlayer());
        }
        UserEntity user = toPartialTransferObject(businessObject);
        user.setPlayer(partialPlayer);
        if (partialPlayer != null) {
            partialPlayer.setOwner(user);
        }
        return user;
    }

    /**
     * Maps a player entity database object to a player business object. This will also map the associated user object,
     * if necessary.
     *
     * @param transferObject database representation of the player
     *
     * @return a player business object equivalent to the transferObject
     */
    public Player toBusinessObject(PlayerEntity transferObject) {
        if (transferObject == null) {
            return null;
        }

        User partialOwner = null;
        if (transferObject.getOwner() != null) {
            partialOwner = toPartialBusinessObject(transferObject.getOwner());
        }
        Player player = toPartialBusinessObject(transferObject);
        player.setOwner(partialOwner);
        if (partialOwner != null) {
            partialOwner.setPlayer(player);
        }
        return player;
    }

    /**
     * Maps a player business object to a player entity database object. This will also map the associated user object,
     * if necessary.
     *
     * @param businessObject business object to map
     *
     * @return a player entity equivalent to the businessObject
     */
    public PlayerEntity toTransferObject(Player businessObject) {
        if (businessObject == null) {
            return null;
        }

        UserEntity partialOwner = null;
        if (businessObject.getOwner() != null) {
            partialOwner = toPartialTransferObject(businessObject.getOwner());
        }
        PlayerEntity player = toPartialTransferObject(businessObject);
        player.setOwner(partialOwner);
        if (partialOwner != null) {
            partialOwner.setPlayer(player);
        }
        return player;
    }

    private Player toPartialBusinessObject(PlayerEntity transferObject) {
        return new Player(
                transferObject.getId(),
                transferObject.getVersion(),
                transferObject.getName(),
                null
        );
    }

    private PlayerEntity toPartialTransferObject(Player businessObject) {
        return new PlayerEntity(
                businessObject.getId(),
                businessObject.getVersion(),
                businessObject.getName(),
                null
        );
    }

    private User toPartialBusinessObject(UserEntity transferObject) {
        return new User(
                transferObject.getId(),
                transferObject.getVersion(),
                transferObject.getName(),
                transferObject.getEmail(),
                transferObject.getPassword(),
                null,
                transferObject.getRolesCopy().stream().map(roleMapper::toBusinessObject).collect(Collectors.toSet())
        );
    }

    private UserEntity toPartialTransferObject(User businessObject) {
        return new UserEntity(
                businessObject.getId(),
                businessObject.getVersion(),
                businessObject.getName(),
                businessObject.getEmail(),
                businessObject.getPassword(),
                null,
                businessObject.getRoles().stream().map(roleMapper::toTransferObject).collect(Collectors.toSet())
        );
    }

}
