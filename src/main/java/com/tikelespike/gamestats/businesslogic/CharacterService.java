package com.tikelespike.gamestats.businesslogic;

import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.CharacterTypeEntity;
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
    private final Mapper<CharacterType, CharacterTypeEntity> typeMapper;


    /**
     * Creates a new character service. This is usually done by the Spring framework, which manages the service's
     * lifecycle and injects the required dependencies.
     *
     * @param characterRepository repository managing character entities in the database
     * @param characterMapper mapper for converting between character business objects and character entities
     * @param typeMapper mapper for converting between character type business objects and character type
     *         entities
     */
    public CharacterService(CharacterRepository characterRepository,
                            Mapper<Character, CharacterEntity> characterMapper,
                            Mapper<CharacterType, CharacterTypeEntity> typeMapper) {
        this.characterRepository = characterRepository;
        this.characterMapper = characterMapper;
        this.typeMapper = typeMapper;
    }

    /**
     * Creates a new character in the system.
     *
     * @param creationRequest the request containing the data for the character to create
     *
     * @return the character as created in the system (now including automatically populated fields)
     */
    public Character createCharacter(CharacterCreationRequest creationRequest) {
        CharacterEntity character = new CharacterEntity(
                null,
                creationRequest.scriptToolIdentifier(),
                creationRequest.name(),
                typeMapper.toTransferObject(creationRequest.characterType()),
                creationRequest.wikiPageLink()
        );
        return characterMapper.toBusinessObject(characterRepository.save(character));
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

    /**
     * Returns the character with the given id if it is known to the system.
     *
     * @param id the id of the character to retrieve
     *
     * @return the character with the given id, or null if no such character exists
     */
    public Character getCharacter(long id) {
        return characterMapper.toBusinessObject(characterRepository.findById(id));
    }

    /**
     * Removes the character with the given id from the system.
     *
     * @param id the id of the character to delete
     */
    public void deleteCharacter(long id) {
        characterRepository.deleteById(id);
    }
}
