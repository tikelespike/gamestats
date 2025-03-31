package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScriptCreationRequestTest {
    private static final Character EXAMPLE_CHARACTER =
            new Character(0L, 0L, "example_character", CharacterType.TOWNSFOLK);

    @Test
    void testCreate() {
        ScriptCreationRequest scriptCreationRequest =
                new ScriptCreationRequest("testCreate_name", "testCreate_description", "testCreate_wikiPage",
                        Set.of(EXAMPLE_CHARACTER));
        assertNotNull(scriptCreationRequest);
        assertEquals("testCreate_name", scriptCreationRequest.name());
        assertEquals("testCreate_description", scriptCreationRequest.description());
        assertEquals("testCreate_wikiPage", scriptCreationRequest.wikiPageLink());
        assertTrue(scriptCreationRequest.characters().contains(EXAMPLE_CHARACTER));
    }

    @Test
    void testCreateMissingName() {
        Set<Character> characters = Set.of(EXAMPLE_CHARACTER);
        assertThrows(NullPointerException.class,
                () -> new ScriptCreationRequest(null, "testCreateMissingName", "testCreateMissingName",
                        characters));
        assertThrows(IllegalArgumentException.class,
                () -> new ScriptCreationRequest("", "testCreateMissingName", "testCreateMissingName",
                        characters));
        assertThrows(IllegalArgumentException.class,
                () -> new ScriptCreationRequest("  ", "testCreateMissingName", "testCreateMissingName",
                        characters));
    }


}
