package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.UserDTO;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.common.Mapper;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

/**
 * Maps between user business objects and their REST transfer representation.
 */
@Component
public class UserMapper extends Mapper<User, UserDTO> {

    private final UserRoleMapper userRoleMapper;

    /**
     * Creates a new UserMapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param userRoleMapper mapper for user roles
     */
    public UserMapper(UserRoleMapper userRoleMapper) {
        super();
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    protected User toBusinessObjectNoCheck(UserDTO transferObject) {
        throw new NotImplementedException(
                "Currently no business object creation from UserDTO is needed, since creation and update endpoints "
                        + "use their own dtos");
    }

    @Override
    protected UserDTO toTransferObjectNoCheck(User businessObject) {
        return new UserDTO(
                businessObject.getId(),
                businessObject.getVersion(),
                businessObject.getName(),
                businessObject.getEmail(),
                userRoleMapper.toTransferObject(businessObject.getRole()),
                businessObject.getPlayer() == null ? null : businessObject.getPlayer().getId()
        );
    }
}
