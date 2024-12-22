package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.SignUpDTO;
import com.tikelespike.gamestats.businesslogic.entities.SignupRequest;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

@Component
public class SignupMapper implements Mapper<SignupRequest, SignUpDTO> {
    @Override
    public SignupRequest toBusinessObject(SignUpDTO transferObject) {
        return new SignupRequest(transferObject.email(), transferObject.password());
    }

    @Override
    public SignUpDTO toTransferObject(SignupRequest businessObject) {
        return new SignUpDTO(businessObject.getEmail(), businessObject.getPassword());
    }
}
