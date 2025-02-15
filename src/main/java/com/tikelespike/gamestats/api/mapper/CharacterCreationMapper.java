package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.CharacterCreationDTO;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between character creation requests and the corresponding transfer objects defining their JSON representation.
 */
@Component
public class CharacterCreationMapper extends Mapper<CharacterCreationRequest, CharacterCreationDTO> {

    private final CharacterTypeMapper typeMapper;

    /**
     * Creates a new character creation mapper. This is usually done by the Spring framework, which manages the mapper's
     * lifecycle and injects the required dependencies.
     *
     * @param typeMapper mapper for character types
     */
    public CharacterCreationMapper(CharacterTypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    protected CharacterCreationRequest toBusinessObjectNoCheck(CharacterCreationDTO transferObject) {
        return new CharacterCreationRequest(
                transferObject.scriptToolIdentifier(),
                transferObject.name(),
                typeMapper.toBusinessObject(transferObject.type()),
                transferObject.wikiPageLink(),
                transferObject.imageUrl()
        );
    }

    @Override
    protected CharacterCreationDTO toTransferObjectNoCheck(CharacterCreationRequest businessObject) {
        return new CharacterCreationDTO(
                businessObject.name(),
                businessObject.scriptToolIdentifier(),
                typeMapper.toTransferObject(businessObject.characterType()),
                businessObject.wikiPageLink(),
                businessObject.imageUrl()
        );
    }
}
