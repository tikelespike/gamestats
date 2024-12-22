package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.UserRole;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.UserRoleEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMapper implements Mapper<UserRole, UserRoleEntity> {
    @Override
    public UserRole toBusinessObject(UserRoleEntity transferObject) {
        return UserRole.valueOf(transferObject.name());
    }

    @Override
    public UserRoleEntity toTransferObject(UserRole businessObject) {
        return UserRoleEntity.valueOf(businessObject.name());
    }
}
