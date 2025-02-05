package com.tikelespike.gamestats.api.controllers;

import com.tikelespike.gamestats.api.entities.CharacterCreationDTO;
import com.tikelespike.gamestats.api.entities.CharacterDTO;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.services.CharacterService;
import com.tikelespike.gamestats.common.Mapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing characters. A character is the role a player takes on within a single game, and what
 * provides the player with abilities. Characters are published by the official Blood on the Clocktower developers, and
 * can be added to this applications database by users with the appropriate permissions. This also allows for the
 * creation of self-made characters that are not part of the official game.
 */
@RestController
@RequestMapping("/api/v1/characters")
@Tag(
        name = "Character Management",
        description = "Operations for managing in-game characters"
)
public class CharacterController {
    private final CharacterService characterService;
    private final Mapper<CharacterCreationRequest, CharacterCreationDTO> creationMapper;
    private final Mapper<Character, CharacterDTO> characterMapper;

    /**
     * Creates a new character controller. This is usually done by the Spring framework, which manages the controller's
     * lifecycle and injects the required dependencies.
     *
     * @param characterService the business layer character service to use for managing characters
     * @param creationMapper the mapper for converting between character creation requests and their JSON
     *         representation
     * @param characterMapper the mapper for converting between character business objects and their JSON
     *         representation
     */
    public CharacterController(CharacterService characterService,
                               Mapper<CharacterCreationRequest, CharacterCreationDTO> creationMapper,
                               Mapper<Character, CharacterDTO> characterMapper) {
        this.characterService = characterService;
        this.creationMapper = creationMapper;
        this.characterMapper = characterMapper;
    }

    /**
     * Creates a new character.
     *
     * @param creationRequest REST dto containing the data
     *
     * @return a REST response entity containing the new character
     */
    @PreAuthorize("hasAuthority('STORYTELLER')")
    @PostMapping()
    public ResponseEntity<Object> createCharacter(@RequestBody CharacterCreationDTO creationRequest) {
        Character character = characterService.createCharacter(creationMapper.toBusinessObject(creationRequest));
        CharacterDTO transferObject = characterMapper.toTransferObject(character);
        URI characterURI = URI.create("/api/v1/characters/" + character.getId());
        return ResponseEntity.created(characterURI).body(transferObject);
    }

    /**
     * Retrieves all characters.
     *
     * @return a REST response entity containing all characters currently known to the system
     */
    @GetMapping()
    public ResponseEntity<Object> getCharacters() {
        List<Character> characters = characterService.getCharacters();
        List<CharacterDTO> transferObjects = characters.stream().map(characterMapper::toTransferObject).toList();
        return ResponseEntity.ok(transferObjects);
    }

    /**
     * Updates a character.
     *
     * @param id character id (must be the same as in the body, if given there)
     * @param characterDTO character to update
     *
     * @return a REST response entity containing the updated character
     */
    @PreAuthorize("hasAuthority('STORYTELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCharacter(@PathVariable("id") long id, @RequestBody CharacterDTO characterDTO) {
        Character character = characterService.updateCharacter(characterMapper.toBusinessObject(characterDTO));
        CharacterDTO transferObject = characterMapper.toTransferObject(character);
        return ResponseEntity.ok(transferObject);
    }
}
