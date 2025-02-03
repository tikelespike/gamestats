package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterTest {

    @Test
    void testCreate() {
        Character character =
                new Character(1L, "testCreate", "testCreate", CharacterType.TOWNSFOLK, "http://testCreate");
        assertNotNull(character);
        assertEquals(1L, character.getId());
        assertEquals("testCreate", character.getScriptToolIdentifier());
        assertEquals("testCreate", character.getName());
        assertEquals(CharacterType.TOWNSFOLK, character.getCharacterType());
        assertEquals("http://testCreate", character.getWikiPageLink());
    }

    @Test
    void testCreateNullId() {
        assertThrows(NullPointerException.class,
                () -> new Character(null, "testCreateNullId", "testCreateNullId", CharacterType.TOWNSFOLK,
                        "http://testCreateNullId"));
    }

    @Test
    void testCreateEmptyName() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, "testCreateEmptyName", null, CharacterType.TOWNSFOLK,
                        "http://testCreateEmptyName"));
        assertThrows(IllegalArgumentException.class,
                () -> new Character(1L, "testCreateEmptyName", "", CharacterType.TOWNSFOLK,
                        "http://testCreateEmptyName"));
        assertThrows(IllegalArgumentException.class,
                () -> new Character(1L, "testCreateEmptyName", "  ", CharacterType.TOWNSFOLK,
                        "http://testCreateEmptyName"));
    }

    @Test
    void testCreateNullCharacterType() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, "testCreateNullCharacterType", "testCreateNullCharacterType", null,
                        "http://testCreateNullCharacterType"));
    }

}
