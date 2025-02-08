package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

    @Test
    void testCreateUnassignedPlayer() {
        Player player = new Player("testCreateUnassignedPlayer");
        assertNotNull(player);
        assertEquals("testCreateUnassignedPlayer", player.getName());
        assertNull(player.getOwner());
    }

    @Test
    void testCreateUnassignedNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Player((String) null));
    }

    @Test
    void testCreateUnassignedEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Player("  "));
    }

    @Test
    void testCreateAssignedPlayer() {
        User owner = new User("testuser_testCreateAssignedPlayer", "e@ma.il", "password", new HashSet<>());
        Player player = new Player(owner);
        assertNotNull(player);
        assertEquals(owner, player.getOwner());
        assertEquals("testuser_testCreateAssignedPlayer", player.getName());
    }

    @Test
    void testCreateAssignedNullOwner() {
        assertThrows(NullPointerException.class, () -> new Player((User) null));
    }

    @Test
    void testCreateNeitherNameNorOwner() {
        assertThrows(IllegalArgumentException.class, () -> new Player(1L, null, null, null));
        assertThrows(IllegalArgumentException.class, () -> new Player(1L, null, " ", null));
    }

}
