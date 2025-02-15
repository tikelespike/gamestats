package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterTest {

    @Test
    void testCreate() {
        Character character =
                new Character(1L, 0L, "testCreate", "testCreate", CharacterType.TOWNSFOLK, "http://testCreate",
                        "http://testCreate/image");
        assertNotNull(character);
        assertEquals(1L, character.getId());
        assertEquals(0L, character.getVersion());
        assertEquals("testCreate", character.getScriptToolIdentifier());
        assertEquals("testCreate", character.getName());
        assertEquals(CharacterType.TOWNSFOLK, character.getCharacterType());
        assertEquals("http://testCreate", character.getWikiPageLink());
        assertEquals("http://testCreate/image", character.getImageUrl());
    }

    @Test
    void testCreateNullId() {
        assertThrows(NullPointerException.class,
                () -> new Character(null, 0L, "testCreateNullId", CharacterType.TOWNSFOLK));
    }

    @Test
    void testCreateNullVersion() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, null, "testCreateNullVersion", CharacterType.TOWNSFOLK));
    }

    @Test
    void testCreateEmptyName() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, 0L, null, CharacterType.TOWNSFOLK));
        assertThrows(IllegalArgumentException.class,
                () -> new Character(1L, 0L, "", CharacterType.TOWNSFOLK));
        assertThrows(IllegalArgumentException.class,
                () -> new Character(1L, 0L, "  ", CharacterType.TOWNSFOLK));
    }

    @Test
    void testCreateNullCharacterType() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, 0L, "testCreateNullCharacterType", null));
    }

    @Test
    void testSetScriptToolIdentifier() {
        Character character = new Character(1L, 0L, "testSetScriptToolIdentifier", "testSetScriptToolIdentifier",
                CharacterType.TOWNSFOLK, "http://testSetScriptToolIdentifier",
                "http://testSetScriptToolIdentifier/image");
        character.setScriptToolIdentifier("testSetScriptToolIdentifier_updated");
        assertEquals("testSetScriptToolIdentifier_updated", character.getScriptToolIdentifier());
    }

    @Test
    void testSetScriptToolIdentifierNull() {
        Character character = new Character(1L, 0L, "testSetScriptToolIdentifierNull",
                "testSetScriptToolIdentifierNull",
                CharacterType.TOWNSFOLK, "http://testSetScriptToolIdentifierNull",
                "http://testSetScriptToolIdentifierNull/image");
        character.setScriptToolIdentifier(null);
        assertNull(character.getScriptToolIdentifier());
    }

    @Test
    void testSetName() {
        Character character = new Character(1L, 0L, "testSetName", CharacterType.TOWNSFOLK);
        character.setName("testSetName_updated");
        assertEquals("testSetName_updated", character.getName());
    }

    @Test
    void testSetNameNull() {
        Character character = new Character(1L, 0L, "testSetNameNull", CharacterType.TOWNSFOLK);
        assertThrows(NullPointerException.class, () -> character.setName(null));
    }

    @Test
    void testSetNameEmpty() {
        Character character = new Character(1L, 0L, "testSetNameEmpty", CharacterType.TOWNSFOLK);
        assertThrows(IllegalArgumentException.class, () -> character.setName(""));
        assertThrows(IllegalArgumentException.class, () -> character.setName("  "));
    }

    @Test
    void testSetCharacterType() {
        Character character = new Character(1L, 0L, "testSetCharacterType", CharacterType.TOWNSFOLK);
        character.setCharacterType(CharacterType.OUTSIDER);
        assertEquals(CharacterType.OUTSIDER, character.getCharacterType());
    }

    @Test
    void testSetCharacterTypeNull() {
        Character character =
                new Character(1L, 0L, "testSetCharacterTypeNull", CharacterType.TOWNSFOLK);
        assertThrows(NullPointerException.class, () -> character.setCharacterType(null));
    }

    @Test
    void testSetWikiPageLink() {
        Character character = new Character(1L, 0L, "testSetWikiPageLink", "testSetWikiPageLink",
                CharacterType.TOWNSFOLK,
                "http://testSetWikiPageLink", "http://testSetWikiPageLink/image");
        character.setWikiPageLink("http://testSetWikiPageLink_updated");
        assertEquals("http://testSetWikiPageLink_updated", character.getWikiPageLink());
    }

    @Test
    void testSetWikiPageLinkNull() {
        Character character =
                new Character(1L, 0L, "testSetWikiPageLinkNull", "testSetWikiPageLinkNull", CharacterType.TOWNSFOLK,
                        "http://testSetWikiPageLinkNull", "http://testSetWikiPageLinkNull/image");
        character.setWikiPageLink(null);
        assertNull(character.getWikiPageLink());
    }

}
