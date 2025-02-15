package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.GamestatsApplication;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GamestatsApplication.class)
class CharacterServiceTest {

    private static final long NON_EXISTENT_ID = 12345L;
    // CUT
    @Autowired
    private CharacterService characterService;

    @Test
    void testCreateCharacter() {
        CharacterCreationRequest request = new CharacterCreationRequest("testCreateCharacter_id",
                "testCreateCharacter_name",
                CharacterType.TOWNSFOLK, "http://testCreateCharacter", "http://testCreateCharacter/image");

        Character newCharacter = characterService.createCharacter(request);

        assertNotNull(newCharacter);
        assertEquals("testCreateCharacter_id", newCharacter.getScriptToolIdentifier());
        assertEquals("testCreateCharacter_name", newCharacter.getName());
        assertEquals(CharacterType.TOWNSFOLK, newCharacter.getCharacterType());
        assertEquals("http://testCreateCharacter", newCharacter.getWikiPageLink());
        assertEquals("http://testCreateCharacter/image", newCharacter.getImageUrl());
    }

    @Test
    void testCreateCharacterNullRequest() {
        assertThrows(NullPointerException.class, () -> characterService.createCharacter(null));
    }


    @Test
    void testUpdateCharacter() throws ResourceNotFoundException, StaleDataException {
        Character character = addTestCharacter("testUpdateCharacter");

        character.setScriptToolIdentifier("testUpdateCharacter_id_updated");
        character.setName("testUpdateCharacter_name_updated");
        character.setWikiPageLink("http://testUpdateCharacter_updated");
        character.setCharacterType(CharacterType.OUTSIDER);

        Character updatedCharacter = characterService.updateCharacter(character);

        assertNotNull(updatedCharacter);
        assertEquals(character, updatedCharacter);
        assertEquals(updatedCharacter, characterService.getCharacter(updatedCharacter.getId()));
    }

    @Test
    void testUpdateCharacterNull() {
        assertThrows(NullPointerException.class, () -> characterService.updateCharacter(null));
    }

    @Test
    void testUpdateCharacterNonExisting() {
        Character character =
                new Character(NON_EXISTENT_ID, 1L, "testUpdateCharacterNonExisting", "testUpdateCharacterNonExisting",
                        CharacterType.TOWNSFOLK, "http://testUpdateCharacterNonExisting",
                        "http://testUpdateCharacterNonExisting/image");
        assertThrows(ResourceNotFoundException.class, () -> characterService.updateCharacter(character));
    }

    @Test
    void testUpdateCharacterOutdated() throws StaleDataException, ResourceNotFoundException {
        Character character = addTestCharacter("testUpdateCharacterOutdated");
        character.setName("testUpdateCharacterOutdated_updated");
        characterService.updateCharacter(character);
        character.setName("testUpdateCharacterOutdated_updated2");
        assertThrows(StaleDataException.class, () -> characterService.updateCharacter(character));
    }


    @Test
    void testGetCharacters() {
        Character character = addTestCharacter("testGetCharacters");

        assertNotNull(characterService.getCharacters());
        assertTrue(characterService.getCharacters().contains(character));
    }

    @Test
    void testGetCharacter() {
        Character character = addTestCharacter("testGetCharacter");

        Character retrievedCharacter = characterService.getCharacter(character.getId());

        assertNotNull(retrievedCharacter);
        assertEquals(character, retrievedCharacter);
    }

    @Test
    void testGetCharacterNonExisting() {
        Character retrievedCharacter = characterService.getCharacter(NON_EXISTENT_ID);

        assertNull(retrievedCharacter);
    }


    @Test
    void testDeleteCharacter() {
        Character character = addTestCharacter("testDeleteCharacter");

        characterService.deleteCharacter(character.getId());

        assertFalse(characterService.getCharacters().contains(character));
    }

    private Character addTestCharacter(String testName) {
        CharacterCreationRequest request = new CharacterCreationRequest(testName + "_id",
                testName + "_name",
                CharacterType.TOWNSFOLK, "http://" + testName, "http://" + testName + "/image");
        return characterService.createCharacter(request);
    }
}
