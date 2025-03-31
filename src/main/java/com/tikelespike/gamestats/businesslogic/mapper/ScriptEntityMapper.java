package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.ScriptEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Maps between the script business object and the script entity database representation.
 */
@Component
public class ScriptEntityMapper extends Mapper<Script, ScriptEntity> {

    private final Mapper<Character, CharacterEntity> characterMapper;

    /**
     * Creates a new mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param characterMapper a mapper for character entities
     */
    public ScriptEntityMapper(Mapper<Character, CharacterEntity> characterMapper) {
        this.characterMapper = characterMapper;
    }

    @Override
    protected Script toBusinessObjectNoCheck(ScriptEntity transferObject) {
        return new Script(
                transferObject.getId(),
                transferObject.getVersion(),
                transferObject.getWikiPageLink(),
                transferObject.getName(),
                transferObject.getDescription(),
                transferObject.getCharacters().stream().map(characterMapper::toBusinessObject)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    protected ScriptEntity toTransferObjectNoCheck(Script businessObject) {
        return new ScriptEntity(
                businessObject.getId(),
                businessObject.getVersion(),
                businessObject.getName(),
                businessObject.getWikiPageLink(),
                businessObject.getDescription(),
                businessObject.getCharacters().stream().map(characterMapper::toTransferObject).toList()
        );
    }
}
