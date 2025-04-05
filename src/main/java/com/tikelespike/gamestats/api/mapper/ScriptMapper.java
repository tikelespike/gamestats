package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.ScriptDTO;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.services.CharacterService;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Maps between script business objects and their REST transfer representation. Does not check if character ids used in
 * the transfer object are valid and simply ignores non-existing character ids.
 */
@Component
public class ScriptMapper extends Mapper<Script, ScriptDTO> {

    private final CharacterService characterService;

    /**
     * Creates a new script mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle
     * and injects the required dependencies.
     *
     * @param characterService service to load full characters from the ids used in the transfer objects
     */
    public ScriptMapper(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    protected Script toBusinessObjectNoCheck(ScriptDTO transferObject) {
        List<Character> characters;
        try {
            characters = characterService.getCharactersByIds(Arrays.stream(transferObject.characterIds()).toList());
        } catch (ResourceNotFoundException e) {
            throw new RelatedResourceNotFoundException(e);
        }
        return new Script(
                transferObject.id(),
                transferObject.version(),
                transferObject.wikiPageLink(),
                transferObject.name(),
                transferObject.description(),
                new HashSet<>(characters)
        );
    }

    @Override
    protected ScriptDTO toTransferObjectNoCheck(Script businessObject) {
        return new ScriptDTO(
                businessObject.getId(),
                businessObject.getVersion(),
                businessObject.getName(),
                businessObject.getDescription(),
                businessObject.getWikiPageLink(),
                businessObject.getCharacters().stream()
                        .map(Character::getId)
                        .toArray(Long[]::new)
        );
    }
}
