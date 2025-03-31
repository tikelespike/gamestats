package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScriptTest {

    @Test
    void testCreate() {
        Character testCharacter = new Character(0L, 0L, "name", CharacterType.TOWNSFOLK);
        Script script = new Script(1L, 0L, "link", "name", "description",
                Set.of(testCharacter));
        assertNotNull(script);
        assertEquals(1L, script.getId());
        assertEquals(0L, script.getVersion());
        assertEquals("link", script.getWikiPageLink());
        assertEquals("name", script.getName());
        assertEquals("description", script.getDescription());
        assertEquals(1, script.getCharacters().size());
        assertTrue(script.getCharacters().contains(testCharacter));
    }

    @Test
    void testCreateMissingName() {
        Character testCharacter = new Character(0L, 0L, "name", CharacterType.TOWNSFOLK);
        assertThrows(NullPointerException.class, () -> new Script(1L, 0L, "link", null, "description",
                Set.of(testCharacter)));
        assertThrows(IllegalArgumentException.class, () -> new Script(1L, 0L, "link", "", "description",
                Set.of(testCharacter)));
        assertThrows(IllegalArgumentException.class, () -> new Script(1L, 0L, "link", "  ", "description",
                Set.of(testCharacter)));
    }

    @Test
    void testCreateMissingCharacters() {
        assertThrows(NullPointerException.class, () -> new Script(1L, 0L, "link", "name", "description",
                null));
        assertThrows(IllegalArgumentException.class, () -> new Script(1L, 0L, "link", "name", "description",
                Set.of()));
    }


    @Test
    void testSetInvalidName() {
        Script script = new Script(1L, 0L, "link", "name", "description",
                Set.of(new Character(0L, 0L, "name", CharacterType.TOWNSFOLK)));
        assertThrows(NullPointerException.class, () -> script.setName(null));
        assertThrows(IllegalArgumentException.class, () -> script.setName(""));
        assertThrows(IllegalArgumentException.class, () -> script.setName("  "));
    }

    @Test
    void testSetInvalidCharacters() {
        Script script = new Script(1L, 0L, "link", "name", "description",
                Set.of(new Character(0L, 0L, "name", CharacterType.TOWNSFOLK)));
        assertThrows(NullPointerException.class, () -> script.setCharacters(null));
        assertThrows(IllegalArgumentException.class, () -> script.setCharacters(Set.of()));
    }
}
