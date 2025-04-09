package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.GamestatsApplication;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.entities.ScriptCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private ScriptService scriptService;

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

    @Test
    void testDeleteCharacters() {
        Character character1 = addTestCharacter("testDeleteCharacters1");
        Character character2 = addTestCharacter("testDeleteCharacters2");

        characterService.deleteCharacters(List.of(character1.getId(), character2.getId()));

        assertNull(characterService.getCharacter(character1.getId()));
        assertNull(characterService.getCharacter(character2.getId()));
    }

    @Test
    void testDeleteCharactersNullRequest() {
        assertThrows(NullPointerException.class, () -> characterService.deleteCharacters(null));
    }

    @Test
    void testDeleteCharactersEmptyList() {
        // Should not throw an exception
        characterService.deleteCharacters(List.of());
    }

    @Test
    void testDeleteCharactersNonExistentIds() {
        // Should not throw an exception
        characterService.deleteCharacters(List.of(NON_EXISTENT_ID));
    }

    @Test
    void testDeleteCharacterRemovesFromScripts() {
        Character character1 = addTestCharacter("testDeleteCharacterRemovesFromScripts_1");
        Character character2 = addTestCharacter("testDeleteCharacterRemovesFromScripts_2");

        Script script = addTestScript("testDeleteCharacterRemovesFromScripts", character1, character2);

        characterService.deleteCharacter(character1.getId());

        Script updatedScript = scriptService.getScript(script.getId());
        assertFalse(updatedScript.getCharacters().contains(character1));
        assertTrue(updatedScript.getCharacters().contains(character2));
    }

    @Test
    void testDeleteCharactersRemovesFromScripts() {
        Character character1 = addTestCharacter("testDeleteCharactersRemovesFromScripts_1");
        Character character2 = addTestCharacter("testDeleteCharactersRemovesFromScripts_2");
        Character character3 = addTestCharacter("testDeleteCharactersRemovesFromScripts_3");

        Script script = addTestScript("testDeleteCharactersRemovesFromScripts", character1, character2, character3);

        characterService.deleteCharacters(List.of(character1.getId(), character2.getId()));

        Script updatedScript = scriptService.getScript(script.getId());
        assertFalse(updatedScript.getCharacters().contains(character1));
        assertFalse(updatedScript.getCharacters().contains(character2));
        assertTrue(updatedScript.getCharacters().contains(character3));
    }

    @Test
    void testDeleteCharacterRemovesFromMultipleScripts() {
        Character character1 = addTestCharacter("testDeleteCharacterRemovesFromMultipleScripts_1");
        Character character2 = addTestCharacter("testDeleteCharacterRemovesFromMultipleScripts_2");

        Script script1 = addTestScript("testDeleteCharacterRemovesFromMultipleScripts_1", character1, character2);
        Script script2 = addTestScript("testDeleteCharacterRemovesFromMultipleScripts_2", character1);

        characterService.deleteCharacter(character1.getId());

        Script updatedScript1 = scriptService.getScript(script1.getId());
        Script updatedScript2 = scriptService.getScript(script2.getId());

        assertFalse(updatedScript1.getCharacters().contains(character1));
        assertTrue(updatedScript1.getCharacters().contains(character2));
        assertFalse(updatedScript2.getCharacters().contains(character1));
    }

    private Character addTestCharacter(String testName) {
        CharacterCreationRequest request = new CharacterCreationRequest(testName + "_id",
                testName + "_name",
                CharacterType.TOWNSFOLK, "http://" + testName, "http://" + testName + "/image");
        return characterService.createCharacter(request);
    }

    private Script addTestScript(String testName, Character... characters) {
        Set<Character> characterSet = new HashSet<>(Arrays.asList(characters));
        ScriptCreationRequest request = new ScriptCreationRequest(
                testName + "_name",
                testName + "_description",
                "http://" + testName,
                characterSet
        );
        return scriptService.createScript(request);
    }
}
