package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.SignUpDTO;
import com.tikelespike.gamestats.businesslogic.entities.SignupRequest;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between the signup request business object and the transfer objects used in the REST interface.
 */
@Component
public class SignupMapper implements Mapper<SignupRequest, SignUpDTO> {
    @Override
    public SignupRequest toBusinessObject(SignUpDTO transferObject) {
        return new SignupRequest(transferObject.email(), transferObject.password());
    }

    @Override
    public SignUpDTO toTransferObject(SignupRequest businessObject) {
        return new SignUpDTO(businessObject.email(), businessObject.password());
    }
}
