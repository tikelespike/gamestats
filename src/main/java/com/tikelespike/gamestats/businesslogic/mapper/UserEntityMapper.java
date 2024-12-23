package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Maps between the user business object and the user entity database representation.
 */
@Component
public class UserEntityMapper implements Mapper<User, UserEntity> {

    private final UserRoleMapper roleMapper;

    /**
     * Creates a new mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param roleMapper mapper for the user roles
     */
    public UserEntityMapper(UserRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public User toBusinessObject(UserEntity transferObject) {
        return new User(
                transferObject.getId(),
                transferObject.getEmail(),
                transferObject.getPassword(),
                transferObject.getRolesCopy().stream().map(roleMapper::toBusinessObject).collect(Collectors.toSet())
        );
    }

    @Override
    public UserEntity toTransferObject(User businessObject) {
        return new UserEntity(
                businessObject.getId(),
                businessObject.getEmail(),
                businessObject.getPassword(),
                businessObject.getRoles().stream().map(roleMapper::toTransferObject).collect(Collectors.toSet())
        );
    }

}
