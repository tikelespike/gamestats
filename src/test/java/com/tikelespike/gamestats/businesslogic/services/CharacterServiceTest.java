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

import java.util.List;

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
    void testUpdateCharacter() throws StaleDataException {
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
    void testUpdateCharacterOutdated() throws StaleDataException {
        Character character = addTestCharacter("testUpdateCharacterOutdated");
        character.setName("testUpdateCharacterOutdated_updated");
        characterService.updateCharacter(character);
        character.setName("testUpdateCharacterOutdated_updated2");
        assertThrows(StaleDataException.class, () -> characterService.updateCharacter(character));
    }


    @Test
    void testGetAllCharacters() {
        Character character = addTestCharacter("testGetCharacters");

        assertNotNull(characterService.getAllCharacters());
        assertTrue(characterService.getAllCharacters().contains(character));
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

        assertFalse(characterService.getAllCharacters().contains(character));
    }

    @Test
    void testCreateCharacters() {
        List<CharacterCreationRequest> requests = List.of(
                new CharacterCreationRequest("test1_id", "test1_name", CharacterType.TOWNSFOLK, "http://test1",
                        "http://test1/image"),
                new CharacterCreationRequest("test2_id", "test2_name", CharacterType.MINION, "http://test2",
                        "http://test2/image")
        );

        List<Character> characters = characterService.createCharacters(requests);

        assertEquals(2, characters.size());
        assertTrue(characters.stream()
                .anyMatch(c -> c.getName().equals("test1_name")));
        assertTrue(characters.stream()
                .anyMatch(c -> c.getName().equals("test2_name")));
        assertTrue(characters.stream()
                .anyMatch(c -> c.getCharacterType() == CharacterType.TOWNSFOLK));
        assertTrue(characters.stream()
                .anyMatch(c -> c.getCharacterType() == CharacterType.MINION));
    }

    @Test
    void testCreateCharactersNullRequest() {
        assertThrows(NullPointerException.class, () -> characterService.createCharacters(null));
    }

    @Test
    void testCreateCharactersEmptyList() {
        List<Character> characters = characterService.createCharacters(List.of());
        assertTrue(characters.isEmpty());
    }

    private Character addTestCharacter(String testName) {
        CharacterCreationRequest request = new CharacterCreationRequest(testName + "_id",
                testName + "_name",
                CharacterType.TOWNSFOLK, "http://" + testName, "http://" + testName + "/image");
        return characterService.createCharacter(request);
    }
}
