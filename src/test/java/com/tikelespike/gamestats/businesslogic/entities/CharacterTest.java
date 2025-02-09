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
                new Character(1L, 0L, "testCreate", "testCreate", CharacterType.TOWNSFOLK, "http://testCreate");
        assertNotNull(character);
        assertEquals(1L, character.getId());
        assertEquals(0L, character.getVersion());
        assertEquals("testCreate", character.getScriptToolIdentifier());
        assertEquals("testCreate", character.getName());
        assertEquals(CharacterType.TOWNSFOLK, character.getCharacterType());
        assertEquals("http://testCreate", character.getWikiPageLink());
    }

    @Test
    void testCreateNullId() {
        assertThrows(NullPointerException.class,
                () -> new Character(null, 0L, "testCreateNullId", "testCreateNullId", CharacterType.TOWNSFOLK,
                        "http://testCreateNullId"));
    }

    @Test
    void testCreateNullVersion() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, null, "testCreateNullVersion", "testCreateNullVersion", CharacterType.TOWNSFOLK,
                        "http://testCreateNullVersion"));
    }

    @Test
    void testCreateEmptyName() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, 0L, "testCreateEmptyName", null, CharacterType.TOWNSFOLK,
                        "http://testCreateEmptyName"));
        assertThrows(IllegalArgumentException.class,
                () -> new Character(1L, 0L, "testCreateEmptyName", "", CharacterType.TOWNSFOLK,
                        "http://testCreateEmptyName"));
        assertThrows(IllegalArgumentException.class,
                () -> new Character(1L, 0L, "testCreateEmptyName", "  ", CharacterType.TOWNSFOLK,
                        "http://testCreateEmptyName"));
    }

    @Test
    void testCreateNullCharacterType() {
        assertThrows(NullPointerException.class,
                () -> new Character(1L, 0L, "testCreateNullCharacterType", "testCreateNullCharacterType", null,
                        "http://testCreateNullCharacterType"));
    }

    @Test
    void testSetScriptToolIdentifier() {
        Character character = new Character(1L, 0L, "testSetScriptToolIdentifier", "testSetScriptToolIdentifier",
                CharacterType.TOWNSFOLK, "http://testSetScriptToolIdentifier");
        character.setScriptToolIdentifier("testSetScriptToolIdentifier_updated");
        assertEquals("testSetScriptToolIdentifier_updated", character.getScriptToolIdentifier());
    }

    @Test
    void testSetScriptToolIdentifierNull() {
        Character character = new Character(1L, 0L, "testSetScriptToolIdentifierNull",
                "testSetScriptToolIdentifierNull",
                CharacterType.TOWNSFOLK, "http://testSetScriptToolIdentifierNull");
        character.setScriptToolIdentifier(null);
        assertNull(character.getScriptToolIdentifier());
    }

    @Test
    void testSetName() {
        Character character = new Character(1L, 0L, "testSetName", "testSetName", CharacterType.TOWNSFOLK,
                "http://testSetName");
        character.setName("testSetName_updated");
        assertEquals("testSetName_updated", character.getName());
    }

    @Test
    void testSetNameNull() {
        Character character = new Character(1L, 0L, "testSetNameNull", "testSetNameNull", CharacterType.TOWNSFOLK,
                "http://testSetNameNull");
        assertThrows(NullPointerException.class, () -> character.setName(null));
    }

    @Test
    void testSetNameEmpty() {
        Character character = new Character(1L, 0L, "testSetNameEmpty", "testSetNameEmpty", CharacterType.TOWNSFOLK,
                "http://testSetNameEmpty");
        assertThrows(IllegalArgumentException.class, () -> character.setName(""));
        assertThrows(IllegalArgumentException.class, () -> character.setName("  "));
    }

    @Test
    void testSetCharacterType() {
        Character character = new Character(1L, 0L, "testSetCharacterType", "testSetCharacterType",
                CharacterType.TOWNSFOLK,
                "http://testSetCharacterType");
        character.setCharacterType(CharacterType.OUTSIDER);
        assertEquals(CharacterType.OUTSIDER, character.getCharacterType());
    }

    @Test
    void testSetCharacterTypeNull() {
        Character character =
                new Character(1L, 0L, "testSetCharacterTypeNull", "testSetCharacterTypeNull", CharacterType.TOWNSFOLK,
                        "http://testSetCharacterTypeNull");
        assertThrows(NullPointerException.class, () -> character.setCharacterType(null));
    }

    @Test
    void testSetWikiPageLink() {
        Character character = new Character(1L, 0L, "testSetWikiPageLink", "testSetWikiPageLink",
                CharacterType.TOWNSFOLK,
                "http://testSetWikiPageLink");
        character.setWikiPageLink("http://testSetWikiPageLink_updated");
        assertEquals("http://testSetWikiPageLink_updated", character.getWikiPageLink());
    }

    @Test
    void testSetWikiPageLinkNull() {
        Character character =
                new Character(1L, 0L, "testSetWikiPageLinkNull", "testSetWikiPageLinkNull", CharacterType.TOWNSFOLK,
                        "http://testSetWikiPageLinkNull");
        character.setWikiPageLink(null);
        assertNull(character.getWikiPageLink());
    }

}
