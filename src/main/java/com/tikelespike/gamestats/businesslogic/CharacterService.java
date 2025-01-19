package com.tikelespike.gamestats.businesslogic;

import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.repositories.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing characters registered within the application.
 */
@Service
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final Mapper<Character, CharacterEntity> characterMapper;

    /**
     * Creates a new character service. This is usually done by the Spring framework, which manages the service's
     * lifecycle and injects the required dependencies.
     *
     * @param characterRepository repository managing character entities in the database
     * @param characterMapper mapper for converting between character business objects and character entities
     */
    public CharacterService(CharacterRepository characterRepository,
                            Mapper<Character, CharacterEntity> characterMapper) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
    }

    /**
     * Creates a new character in the system.
     *
     * @param creationRequest the request containing the data for the character to create
     *
     * @return the character as created in the system (now including automatically populated fields)
     */
    public Character createCharacter(CharacterCreationRequest creationRequest) {
        Character character = new Character(
                null,
                creationRequest.scriptToolIdentifier(),
                creationRequest.name(),
                creationRequest.characterType(),
                creationRequest.wikiPageLink()
        );
        return characterMapper.toBusinessObject(characterRepository.save(characterMapper.toTransferObject(character)));
    }

    /**
     * Updates an existing character in the system.
     *
     * @param character the character to update
     *
     * @return the updated character
     */
    public Character updateCharacter(Character character) {
        return characterMapper.toBusinessObject(characterRepository.save(characterMapper.toTransferObject(character)));
    }

    /**
     * Returns the list of characters currently known to the system.
     *
     * @return the list of characters currently known to the system.
     */
    public List<Character> getCharacters() {
        return characterRepository.findAll().stream().map(characterMapper::toBusinessObject).toList();
    }
}
