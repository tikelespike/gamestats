package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.CharacterTypeEntity;
import com.tikelespike.gamestats.data.repositories.CharacterRepository;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
     * @param creationRequest the request containing the data for the character to create. May not be null.
     *
     * @return the character as created in the system (now including automatically populated fields)
     */
    public Character createCharacter(CharacterCreationRequest creationRequest) {
        CharacterEntity character = new CharacterEntity(
                null,
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
     * @param character the character to update. A character with the same id must already exist in the system.
     *
     * @return the updated character
     * @throws ResourceNotFoundException if the character with the given id does not exist
     * @throws StaleDataException if the character has been modified or deleted in the meantime (concurrently)
     */
    public Character updateCharacter(Character character) throws ResourceNotFoundException, StaleDataException {
        if (characterRepository.findById(character.getId()) == null) {
            throw new ResourceNotFoundException("Character with id " + character.getId() + " does not exist");
        }
        CharacterEntity entityToSave = characterMapper.toTransferObject(character);
        CharacterEntity savedEntity;
        try {
            savedEntity = characterRepository.save(entityToSave);
        } catch (StaleObjectStateException | OptimisticLockException | ObjectOptimisticLockingFailureException e) {
            throw new StaleDataException(e);
        }
        return characterMapper.toBusinessObject(savedEntity);
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
