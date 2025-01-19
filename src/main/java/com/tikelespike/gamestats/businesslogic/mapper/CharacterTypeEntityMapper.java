package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterTypeEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between the character type business object and the character type entity database representation.
 */
@Component
public class CharacterTypeEntityMapper extends Mapper<CharacterType, CharacterTypeEntity> {

    @Override
    public CharacterType toBusinessObjectNoCheck(CharacterTypeEntity transferObject) {
        return CharacterType.valueOf(transferObject.name());
    }

    @Override
    public CharacterTypeEntity toTransferObjectNoCheck(CharacterType businessObject) {
        return CharacterTypeEntity.valueOf(businessObject.name());
    }
}
