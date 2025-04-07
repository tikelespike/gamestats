package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.ScriptCreationDTO;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.ScriptCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.services.CharacterService;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Maps between script creation business objects and their REST transfer representation. Does not check if the
 * characters are valid for the script and simply ignores non-existing character ids.
 */
@Component
public class ScriptCreationMapper extends Mapper<ScriptCreationRequest, ScriptCreationDTO> {

    private final CharacterService characterService;

    /**
     * Creates a new script creation mapper. This is usually done by the Spring framework, which manages the mapper's
     * lifecycle and injects the required dependencies.
     *
     * @param characterService service to load full characters from the ids used in the transfer objects
     */
    public ScriptCreationMapper(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    protected ScriptCreationRequest toBusinessObjectNoCheck(ScriptCreationDTO transferObject) {
        Long[] ids = transferObject.characterIds() == null ? new Long[0] : transferObject.characterIds();

        List<Character> characters;
        try {
            characters = characterService.getCharactersByIds(Arrays.stream(ids).toList());
        } catch (ResourceNotFoundException e) {
            throw new RelatedResourceNotFoundException(e);
        }

        return new ScriptCreationRequest(
                transferObject.name(),
                transferObject.description(),
                transferObject.wikiPageLink(),
                new HashSet<>(characters)
        );
    }

    @Override
    protected ScriptCreationDTO toTransferObjectNoCheck(ScriptCreationRequest businessObject) {
        return new ScriptCreationDTO(
                businessObject.name(),
                businessObject.description(),
                businessObject.wikiPageLink(),
                businessObject.characters().stream()
                        .map(Character::getId)
                        .toArray(Long[]::new)
        );
    }
}
