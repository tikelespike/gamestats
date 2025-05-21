package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.UserRoleDTO;
import com.tikelespike.gamestats.businesslogic.entities.UserRole;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Maps between the user role business object and the transfer objects used in the REST interface.
 */
@Component
public class UserRoleMapper extends Mapper<UserRole, UserRoleDTO> {
    @Override
    protected UserRole toBusinessObjectNoCheck(UserRoleDTO transferObject) {
        return UserRole.valueOf(transferObject.name().toUpperCase(Locale.ROOT));
    }

    @Override
    protected UserRoleDTO toTransferObjectNoCheck(UserRole businessObject) {
        return UserRoleDTO.valueOf(businessObject.name().toLowerCase(Locale.ROOT));
    }
}
