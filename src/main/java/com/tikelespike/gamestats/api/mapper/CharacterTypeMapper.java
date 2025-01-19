package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.CharacterTypeDTO;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Maps between the character type business object and the character type transfer object.
 */
@Component
public class CharacterTypeMapper extends Mapper<CharacterType, CharacterTypeDTO> {

    @Override
    protected CharacterType toBusinessObjectNoCheck(CharacterTypeDTO transferObject) {
        return CharacterType.valueOf(transferObject.name().toUpperCase(Locale.ROOT));
    }

    @Override
    protected CharacterTypeDTO toTransferObjectNoCheck(CharacterType businessObject) {
        return CharacterTypeDTO.valueOf(businessObject.name().toLowerCase(Locale.ROOT));
    }
}
