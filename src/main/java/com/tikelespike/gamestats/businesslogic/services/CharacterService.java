package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.CharacterTypeEntity;
import com.tikelespike.gamestats.data.entities.ScriptEntity;
import com.tikelespike.gamestats.data.repositories.CharacterRepository;
import com.tikelespike.gamestats.data.repositories.ScriptRepository;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service class for managing characters registered within the application.
 */
@Service
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final ScriptRepository scriptRepository;
    private final Mapper<Character, CharacterEntity> characterMapper;
    private final Mapper<CharacterType, CharacterTypeEntity> typeMapper;


    /**
     * Creates a new character service. This is usually done by the Spring framework, which manages the service's
     * lifecycle and injects the required dependencies.
     *
     * @param characterRepository repository managing character entities in the database
     * @param scriptRepository repository managing script entities in the database
     * @param characterMapper mapper for converting between character business objects and character entities
     * @param typeMapper mapper for converting between character type business objects and character type
     *         entities
     */
    public CharacterService(CharacterRepository characterRepository, ScriptRepository scriptRepository,
                            Mapper<Character, CharacterEntity> characterMapper,
                            Mapper<CharacterType, CharacterTypeEntity> typeMapper) {
        this.characterRepository = characterRepository;
        this.scriptRepository = scriptRepository;
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
        CharacterEntity character = createEntityFromCreationRequest(creationRequest);
        return characterMapper.toBusinessObject(characterRepository.save(character));
    }

    /**
     * Creates multiple characters in a single atomic transaction. If any character creation fails, none of the
     * characters will be created.
     *
     * @param creationRequests list of character creation requests. May not be null.
     *
     * @return list of all created characters
     */
    @Transactional
    public List<Character> createCharacters(List<CharacterCreationRequest> creationRequests) {
        Objects.requireNonNull(creationRequests, "Creation requests may not be null");

        List<CharacterEntity> characters = creationRequests.stream()
                .map(this::createEntityFromCreationRequest)
                .toList();

        Iterable<CharacterEntity> savedEntities = characterRepository.saveAll(characters);
        List<CharacterEntity> savedCharacters = new ArrayList<>();
        savedEntities.forEach(savedCharacters::add);

        return savedCharacters.stream()
                .map(characterMapper::toBusinessObject)
                .toList();
    }

    private CharacterEntity createEntityFromCreationRequest(CharacterCreationRequest creationRequest) {
        return new CharacterEntity(
                null,
                null,
                creationRequest.scriptToolIdentifier(),
                creationRequest.name(),
                typeMapper.toTransferObject(creationRequest.characterType()),
                creationRequest.wikiPageLink(),
                creationRequest.imageUrl()
        );
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
    public List<Character> getAllCharacters() {
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
     * Returns the list of characters with the given ids.
     *
     * @param ids the ids of the characters to retrieve
     *
     * @return the list of characters with the given ids
     * @throws ResourceNotFoundException if at least one of the characters with the given ids does not exist
     */
    public List<Character> getCharactersByIds(List<Long> ids) {
        List<Character> foundCharacters = getAllCharacters().stream().filter(c -> ids.contains(c.getId())).toList();
        if (foundCharacters.size() != ids.size()) {
            throw new ResourceNotFoundException("At least one of the characters with the given ids does not exist");
        }
        return foundCharacters;
    }

    /**
     * Removes the character with the given id from the system. No effect if no such character exists.
     *
     * @param id the id of the character to delete
     */
    @Transactional
    public void deleteCharacter(long id) {
        CharacterEntity character = characterRepository.findById(id);
        if (character == null) {
            return;
        }

        List<ScriptEntity> scriptsWithCharacter = scriptRepository.findAllByCharactersContains(List.of(character));
        for (ScriptEntity script : scriptsWithCharacter) {
            script.removeCharacter(character);
            scriptRepository.save(script);
        }

        characterRepository.deleteById(id);
        // Note: Committing this transaction will fail if the character is concurrently re-added into one of the
        // scripts (or a new script with it is created) inbetween removing the character from the scripts and
        // deleting the character (due to referential integrity violation). We accept this for now instead of adding
        // more pessimistic locking and risking creating deadlocks accidentally.
    }

    /**
     * Deletes multiple characters in a single atomic transaction. If any character deletion fails, none of the
     * characters will be deleted.
     *
     * @param ids list of character IDs to delete. May not be null.
     */
    @Transactional
    public void deleteCharacters(List<Long> ids) {
        Objects.requireNonNull(ids, "Character IDs may not be null");

        List<CharacterEntity> charactersToDelete = ids.stream()
                .map(characterRepository::findById)
                .filter(Objects::nonNull)
                .toList();

        if (charactersToDelete.isEmpty()) {
            return;
        }

        for (CharacterEntity character : charactersToDelete) {
            List<ScriptEntity> scriptsWithCharacter = scriptRepository.findAllByCharactersContains(List.of(character));

            for (ScriptEntity script : scriptsWithCharacter) {
                script.removeCharacter(character);
                scriptRepository.save(script);
            }

            characterRepository.deleteById(character.getId());
        }
    }
}
