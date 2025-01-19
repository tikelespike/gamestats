package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.CharacterTypeEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between the character business object and the character entity database representation.
 */
@Component
public class CharacterEntityMapper extends Mapper<Character, CharacterEntity> {

    private final Mapper<CharacterType, CharacterTypeEntity> typeMapper;

    /**
     * Creates a new mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param typeMapper mapper for character types
     */
    public CharacterEntityMapper(Mapper<CharacterType, CharacterTypeEntity> typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public Character toBusinessObjectNoCheck(CharacterEntity transferObject) {
        return new Character(
                transferObject.getId(),
                transferObject.getScriptToolIdentifier(),
                transferObject.getName(),
                typeMapper.toBusinessObject(transferObject.getCharacterType()),
                transferObject.getWikiPageLink()
        );
    }

    @Override
    public CharacterEntity toTransferObjectNoCheck(Character businessObject) {
        return new CharacterEntity(
                businessObject.getId(),
                businessObject.getScriptToolIdentifier(),
                businessObject.getName(),
                typeMapper.toTransferObject(businessObject.getCharacterType()),
                businessObject.getWikiPageLink()
        );
    }
}
