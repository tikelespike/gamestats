package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.SignUpDTO;
import com.tikelespike.gamestats.businesslogic.entities.SignupRequest;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between the signup request business object and the transfer objects used in the REST interface.
 */
@Component
public class SignupMapper extends Mapper<SignupRequest, SignUpDTO> {
    @Override
    public SignupRequest toBusinessObjectNoCheck(SignUpDTO transferObject) {
        return new SignupRequest(transferObject.name(), transferObject.email(), transferObject.password());
    }

    @Override
    public SignUpDTO toTransferObjectNoCheck(SignupRequest businessObject) {
        return new SignUpDTO(businessObject.name(), businessObject.email(), businessObject.password());
    }
}
