package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.CharacterDTO;
import com.tikelespike.gamestats.api.entities.CharacterTypeDTO;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between the character business object and the character transfer object used in the REST interface.
 */
@Component
public class CharacterMapper extends Mapper<Character, CharacterDTO> {
    private final Mapper<CharacterType, CharacterTypeDTO> characterTypeMapper;

    /**
     * Creates a new character mapper. This is usually done by the Spring framework, which manages the mapper's
     * lifecycle and injects the required dependencies.
     *
     * @param characterTypeMapper mapper for character types
     */
    public CharacterMapper(CharacterTypeMapper characterTypeMapper) {
        this.characterTypeMapper = characterTypeMapper;
    }

    @Override
    protected Character toBusinessObjectNoCheck(CharacterDTO transferObject) {
        return new Character(
                transferObject.id(),
                transferObject.scriptToolIdentifier(),
                transferObject.name(),
                characterTypeMapper.toBusinessObject(transferObject.type()),
                transferObject.wikiPageLink()
        );
    }

    @Override
    protected CharacterDTO toTransferObjectNoCheck(Character businessObject) {
        return new CharacterDTO(
                businessObject.getId(),
                businessObject.getName(),
                businessObject.getScriptToolIdentifier(),
                characterTypeMapper.toTransferObject(businessObject.getCharacterType()),
                businessObject.getWikiPageLink()
        );
    }
}
