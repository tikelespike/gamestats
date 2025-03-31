package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.entities.ScriptCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.ScriptEntity;
import com.tikelespike.gamestats.data.repositories.CharacterRepository;
import com.tikelespike.gamestats.data.repositories.ScriptRepository;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing collections of characters.
 */
@Service
public class ScriptService {

    private final ScriptRepository scriptRepository;
    private final CharacterRepository characterRepository;
    private final Mapper<Script, ScriptEntity> scriptMapper;
    private final Mapper<Character, CharacterEntity> characterMapper;

    /**
     * Creates a new script service. This is usually done by the Spring framework, which manages the service's lifecycle
     * and injects the required dependencies.
     *
     * @param scriptRepository repository managing script entities in the database
     * @param characterRepository repository managing character entities in the database
     * @param scriptMapper mapper for converting between script business objects and script entities
     * @param characterMapper mapper for converting between character business objects and character entities
     */
    public ScriptService(ScriptRepository scriptRepository, CharacterRepository characterRepository,
                         Mapper<Script, ScriptEntity> scriptMapper,
                         Mapper<Character, CharacterEntity> characterMapper) {
        this.scriptRepository = scriptRepository;
        this.characterRepository = characterRepository;
        this.scriptMapper = scriptMapper;
        this.characterMapper = characterMapper;
    }

    /**
     * Creates a new script in the system.
     *
     * @param scriptCreationRequest the request containing the data for the script to create. May not be null.
     *
     * @return the new script as created in the system (now including automatically populated fields)
     * @throws ResourceNotFoundException if any of the characters in the script do not exist in the database
     */
    @Transactional(rollbackFor = {ResourceNotFoundException.class})
    public Script createScript(ScriptCreationRequest scriptCreationRequest) throws ResourceNotFoundException {
        ScriptEntity scriptEntity = new ScriptEntity(
                null,
                null,
                scriptCreationRequest.name(),
                scriptCreationRequest.wikiPageLink(),
                scriptCreationRequest.description(),
                scriptCreationRequest.characters().stream().map(characterMapper::toTransferObject).toList()
        );

        // Check if characters exist in the database
        List<CharacterEntity> existingCharacters = characterRepository.findAllByIdWithLock(
                scriptCreationRequest.characters().stream().map(Character::getId).toList());
        if (existingCharacters.size() != scriptCreationRequest.characters().size()) {
            throw new ResourceNotFoundException("At least one of the characters of the script does not exist");
        }

        ScriptEntity savedScriptEntity = scriptRepository.save(scriptEntity);

        return scriptMapper.toBusinessObject(savedScriptEntity);
    }

    /**
     * Retrieves a script by its ID.
     *
     * @param id the ID of the script to retrieve
     *
     * @return the script with the given ID, or null if no such script exists
     */
    public Script getScript(long id) {
        ScriptEntity scriptEntity = scriptRepository.findById(id);
        return scriptMapper.toBusinessObject(scriptEntity);
    }

    /**
     * Retrieves all scripts in the system.
     *
     * @return a list of all scripts
     */
    public List<Script> getAllScripts() {
        List<ScriptEntity> scriptEntities = scriptRepository.findAll();
        return scriptEntities.stream()
                .map(scriptMapper::toBusinessObject)
                .toList();
    }

    /**
     * Updates an existing script in the system.
     *
     * @param script the script to update. A script with the same id must already exist in the system.
     *
     * @return the updated script
     * @throws ResourceNotFoundException if the script with the given id does not exist
     * @throws StaleDataException if the script has been modified or deleted in the meantime (concurrently)
     */
    @Transactional(rollbackFor = {ResourceNotFoundException.class, StaleDataException.class})
    public Script updateScript(Script script) throws ResourceNotFoundException, StaleDataException {
        if (scriptRepository.findById(script.getId()) == null) {
            throw new ResourceNotFoundException("Script with id " + script.getId() + " does not exist");
        }

        // Check if characters exist in the database
        List<CharacterEntity> existingCharacters = characterRepository.findAllByIdWithLock(
                script.getCharacters().stream().map(Character::getId).toList());
        if (existingCharacters.size() != script.getCharacters().size()) {
            throw new ResourceNotFoundException("At least one of the characters of the script does not exist");
        }

        ScriptEntity entityToSave = scriptMapper.toTransferObject(script);
        ScriptEntity savedEntity;
        try {
            savedEntity = scriptRepository.save(entityToSave);
        } catch (StaleObjectStateException | OptimisticLockException | ObjectOptimisticLockingFailureException e) {
            throw new StaleDataException(e);
        }
        return scriptMapper.toBusinessObject(savedEntity);
    }

    /**
     * Removes a script from the system. No effect if the script does not exist.
     *
     * @param id the ID of the script to delete
     */
    public void deleteScript(long id) {
        scriptRepository.deleteById(id);
    }
}
