package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.GamestatsApplication;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.entities.ScriptCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GamestatsApplication.class)
class ScriptServiceTest {
    private static final int TEST_CHARACTER_AMOUNT = 3;
    private static final long NON_EXISTENT_ID = 999L;
    private static Set<Character> testCharacters;
    // CUT
    @Autowired
    private ScriptService scriptService;

    @Autowired
    private CharacterService characterService;

    @BeforeAll
    static void setUp(@Autowired CharacterService characterService) {
        testCharacters = new HashSet<>();
        for (int i = 0; i < TEST_CHARACTER_AMOUNT; i++) {
            CharacterCreationRequest characterRequest = new CharacterCreationRequest("testcharacter_" + i,
                    "Test Character " + i, CharacterType.TOWNSFOLK, "http://test.character/wiki/" + i,
                    "http://test.character/image/" + i);
            Character character = characterService.createCharacter(characterRequest);
            testCharacters.add(character);
        }
    }

    @Test
    void testCreateScript() {
        ScriptCreationRequest request = new ScriptCreationRequest("testCreateScript_name",
                "testCreateScript_description", "testCreateScript_wikiPage", testCharacters);

        Script newScript = scriptService.createScript(request);

        assertNotNull(newScript);
        assertEquals("testCreateScript_name", newScript.getName());
        assertEquals("testCreateScript_description", newScript.getDescription());
        assertEquals("testCreateScript_wikiPage", newScript.getWikiPageLink());
        assertArrayEquals(testCharacters.toArray(), newScript.getCharacters().toArray());
    }

    @Test
    void testCreateScriptNullRequest() {
        assertThrows(NullPointerException.class, () -> scriptService.createScript(null));
    }

    @Test
    void testCreateScriptNonExistingCharacter() {
        Set<Character> characters = new HashSet<>(testCharacters);
        characters.add(new Character(NON_EXISTENT_ID, 0L, "nonExistingCharacter", "Non Existing Character",
                CharacterType.TOWNSFOLK, "http://non.existing.character/wiki", "http://non.existing.character/image"));

        ScriptCreationRequest request = new ScriptCreationRequest("testCreateScriptNonExistingCharacter_name",
                "testCreateScriptNonExistingCharacter_description", "testCreateScriptNonExistingCharacter_wikiPage",
                characters);

        assertThrows(RelatedResourceNotFoundException.class, () -> scriptService.createScript(request));
    }


    @Test
    void testGetScript() {
        Script newScript = addTestScript("testGetScript");

        Script retrievedScript = scriptService.getScript(newScript.getId());

        assertEquals(newScript, retrievedScript);
    }

    @Test
    void testGetScriptNonExisting() {
        assertNull(scriptService.getScript(NON_EXISTENT_ID));
    }

    @Test
    void testGetAllScripts() {
        Script script = addTestScript("testGetAllScripts");

        assertTrue(scriptService.getAllScripts().contains(script));
    }


    @Test
    void testUpdateScript() throws StaleDataException {
        Script script = addTestScript("testUpdateScript");

        script.setName("updatedName");
        script.setDescription("updatedDescription");
        script.setWikiPageLink("updatedWikiPage");

        Script updatedScript = scriptService.updateScript(script);

        assertEquals("updatedName", updatedScript.getName());
        assertEquals("updatedDescription", updatedScript.getDescription());
        assertEquals("updatedWikiPage", updatedScript.getWikiPageLink());
    }

    @Test
    void testUpdateScriptNull() {
        assertThrows(NullPointerException.class, () -> scriptService.updateScript(null));
    }

    @Test
    void testUpdateScriptNonExisting() {
        Script script = new Script(NON_EXISTENT_ID, 0L, "testUpdateScriptNonExisting", "testUpdateScriptNonExisting",
                "testUpdateScriptNonExisting", testCharacters);

        assertThrows(ResourceNotFoundException.class, () -> scriptService.updateScript(script));
    }

    @Test
    void testUpdateScriptOutdated() throws StaleDataException {
        Script script = addTestScript("testUpdateScriptOutdated");
        script.setName("updatedName");
        scriptService.updateScript(script);
        script.setName("updatedName2");

        assertThrows(StaleDataException.class, () -> scriptService.updateScript(script));
    }

    @Test
    void testUpdateScriptWithNonExistingCharacter() {
        Script script = addTestScript("testUpdateScriptWithNonExistingCharacter");

        Set<Character> characters = new HashSet<>(script.getCharacters());
        characters.add(new Character(NON_EXISTENT_ID, 0L, "nonExistingCharacter", "Non Existing Character",
                CharacterType.TOWNSFOLK, "http://non.existing.character/wiki", "http://non.existing.character/image"));
        script.setCharacters(characters);

        assertThrows(RelatedResourceNotFoundException.class, () -> scriptService.updateScript(script));
    }

    @Test
    void testDeleteScript() {
        Script script = addTestScript("testDeleteScript");

        scriptService.deleteScript(script.getId());

        assertNull(scriptService.getScript(script.getId()));
    }

    private Script addTestScript(String identifier) {
        ScriptCreationRequest request = new ScriptCreationRequest(identifier + "_name",
                identifier + "testUpdateScript_description", identifier + "testUpdateScript_wikiPage", testCharacters);

        return scriptService.createScript(request);
    }

}
