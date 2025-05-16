package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.UserCreationDTO;
import com.tikelespike.gamestats.api.entities.UserRoleDTO;
import com.tikelespike.gamestats.businesslogic.entities.SignupRequest;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between the signup request business object and the transfer objects used in the REST interface.
 */
@Component
public class UserCreationMapper extends Mapper<SignupRequest, UserCreationDTO> {
    @Override
    public SignupRequest toBusinessObjectNoCheck(UserCreationDTO transferObject) {
        return new SignupRequest(transferObject.name(), transferObject.email(), transferObject.password());
    }

    @Override
    public UserCreationDTO toTransferObjectNoCheck(SignupRequest businessObject) {
        return new UserCreationDTO(businessObject.name(), businessObject.email(), businessObject.password(),
                null); // TODO: implement permission level in business layer
    }
}
