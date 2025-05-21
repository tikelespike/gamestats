package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.UserCreationDTO;
import com.tikelespike.gamestats.api.entities.UserRoleDTO;
import com.tikelespike.gamestats.businesslogic.entities.UserCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.UserRole;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between the signup request business object and the transfer objects used in the REST interface.
 */
@Component
public class UserCreationMapper extends Mapper<UserCreationRequest, UserCreationDTO> {

    private final Mapper<UserRole, UserRoleDTO> userRoleMapper;

    /**
     * Creates a new user creation request mapper. This is usually done by the Spring framework, which manages the
     * mapper's lifecycle and injects the required dependencies.
     *
     * @param userRoleMapper mapper for user roles
     */
    public UserCreationMapper(Mapper<UserRole, UserRoleDTO> userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public UserCreationRequest toBusinessObjectNoCheck(UserCreationDTO transferObject) {
        return new UserCreationRequest(transferObject.name(), transferObject.email(), transferObject.password(),
                userRoleMapper.toBusinessObject(transferObject.permissionLevel()));
    }

    @Override
    public UserCreationDTO toTransferObjectNoCheck(UserCreationRequest businessObject) {
        return new UserCreationDTO(businessObject.name(), businessObject.email(), businessObject.password(),
                userRoleMapper.toTransferObject(businessObject.role()));
    }
}
