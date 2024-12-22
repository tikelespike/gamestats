package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserEntityMapper implements Mapper<User, UserEntity> {

    private final UserRoleMapper roleMapper;

    public UserEntityMapper(UserRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public User toBusinessObject(UserEntity transferObject) {
        return new User(transferObject.getId(), transferObject.getEmail(), transferObject.getPassword(),
                transferObject.getRoles().stream().map(roleMapper::toBusinessObject).collect(
                        Collectors.toSet()));
    }

    @Override
    public UserEntity toTransferObject(User businessObject) {
        UserEntity entity = new UserEntity();
        entity.setId(businessObject.getId());
        entity.setEmail(businessObject.getEmail());
        entity.setPassword(businessObject.getPassword());
        entity.setRoles(businessObject.getRoles().stream().map(roleMapper::toTransferObject).collect(
                Collectors.toSet()));
        return entity;
    }

}
