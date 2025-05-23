package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.UserUpdateDTO;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.services.PlayerService;
import com.tikelespike.gamestats.businesslogic.services.UserService;
import com.tikelespike.gamestats.common.Mapper;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Maps between user update data transfer objects and user business objects.
 */
@Component
public class UserUpdateMapper extends Mapper<User, UserUpdateDTO> {

    private final UserRoleMapper userRoleMapper;
    private final UserService userService;
    private final PlayerService playerService;

    /**
     * Creates a new UserUpdateMapper. This is usually done by the Spring framework, which manages the mapper's
     * lifecycle and injects the required dependencies.
     *
     * @param userRoleMapper mapper for user roles
     * @param userService service to load old user state
     * @param playerService service for managing players
     */
    public UserUpdateMapper(UserRoleMapper userRoleMapper, UserService userService, PlayerService playerService) {
        this.userRoleMapper = userRoleMapper;
        this.userService = userService;
        this.playerService = playerService;
    }

    @Override
    protected User toBusinessObjectNoCheck(UserUpdateDTO transferObject) {
        String newPassword;
        if (transferObject.password() != null) {
            newPassword = new BCryptPasswordEncoder().encode(transferObject.password());
        } else {
            User existingUser = userService.getUser(transferObject.id());
            if (existingUser == null) {
                throw new ResourceNotFoundException("User with id " + transferObject.id() + " does not exist");
            }
            newPassword = existingUser.getPassword();
        }

        Player player = null;
        if (transferObject.playerId() != null) {
            player = playerService.getPlayerById(transferObject.playerId());
            if (player == null) {
                throw new RelatedResourceNotFoundException(
                        "Player with id " + transferObject.playerId() + " not found");
            }
        }

        return new User(
                transferObject.id(),
                transferObject.version(),
                transferObject.name(),
                transferObject.email(),
                newPassword,
                player,
                userRoleMapper.toBusinessObject(transferObject.permissionLevel())
        );
    }

    @Override
    protected UserUpdateDTO toTransferObjectNoCheck(User businessObject) {
        throw new NotImplementedException("Currently no transfer object creation from User is needed");
    }
}
