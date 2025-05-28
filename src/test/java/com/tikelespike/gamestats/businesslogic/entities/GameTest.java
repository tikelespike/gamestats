package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private static final long NUMBER_3 = 3L; // crude checkstyle magic number bypass
    private static final int MAX_DESCRIPTION_LENGTH = 5000;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Script script;
    private List<PlayerParticipation> participants;
    private Character character1;
    private Character character2;
    private Character character3;

    @BeforeEach
    void setUp() {
        player1 = new Player(0L, 0L, "Player1", null);
        player2 = new Player(1L, 0L, "Player2", null);
        player3 = new Player(2L, 0L, "Player3", null);
        player4 = new Player(NUMBER_3, 0L, "Player4", null);

        character1 = new Character(1L, 1L, "Character1", CharacterType.TOWNSFOLK);
        character2 = new Character(2L, 1L, "Character2", CharacterType.MINION);
        character3 = new Character(NUMBER_3, 1L, "Character3", CharacterType.TOWNSFOLK);

        script = new Script(1L, 1L, null, "Test Script", "Test Description",
                new HashSet<>(Arrays.asList(character1, character2, character3)));

        participants = Arrays.asList(
                new PlayerParticipation(player1, character1, true),
                new PlayerParticipation(player2, character2, true),
                new PlayerParticipation(player3, character3, true)
        );
    }

    @Test
    void testConstructorWithAlignment() {
        Game game =
                new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name", List.of(player4));
        assertNotNull(game);
        assertEquals(1L, game.getId());
        assertEquals(1L, game.getVersion());
        assertEquals(participants, game.getParticipants());
        assertEquals(script, game.getScript());
        assertEquals(Alignment.GOOD, game.getWinningAlignment());
        assertEquals("Test game", game.getDescription());
        assertEquals("Test game name", game.getName());
        assertEquals(2, game.getWinningPlayers().size());
        assertEquals(1, game.getStorytellers().size());
        assertEquals(player4, game.getStorytellers().getFirst());
    }

    @Test
    void testConstructorWithWinningPlayers() {
        List<Player> winners = Arrays.asList(player1, player3);
        Game game = new Game(1L, 1L, participants, script, "Test game", winners, "Test game name", List.of(player4));
        assertNotNull(game);
        assertEquals(winners, game.getWinningPlayers());
        assertNull(game.getWinningAlignment());
    }

    @Test
    void testDuplicateParticipants() {
        List<PlayerParticipation> duplicateParticipants = new ArrayList<>(participants);
        duplicateParticipants.add(new PlayerParticipation(player1, character2, true));

        assertThrows(IllegalArgumentException.class, () ->
                new Game(1L, 1L, duplicateParticipants, script, Alignment.GOOD, "Test game", "Test game name",
                        List.of(player4))
        );
    }

    @Test
    void testNullScript() {
        Game game =
                new Game(1L, 1L, participants, null, Alignment.GOOD, "Test game", "Test game name", List.of(player4));
        assertNull(game.getScript());
    }

    @Test
    void testNullWinningAlignment() {
        assertThrows(NullPointerException.class, () ->
                new Game(1L, 1L, participants, script, null, "Test game", "Test game name", List.of(player4))
        );
    }

    @Test
    void testGetWinningPlayersWithAlignment() {
        Game game = new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name",
                List.of(player4));
        List<Player> winners = game.getWinningPlayers();

        assertEquals(2, winners.size());
        assertTrue(winners.contains(player1));
        assertTrue(winners.contains(player3));
        assertFalse(winners.contains(player2));
    }

    @Test
    void testSetWinningPlayersResetsAlignment() {
        Game game =
                new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name", List.of(player4));
        List<Player> winners = Arrays.asList(player1, player2);

        game.setWinningPlayers(winners);
        assertNull(game.getWinningAlignment());
        assertEquals(winners, game.getWinningPlayers());
    }

    @Test
    void testSetParticipantsUpdatesWinningPlayers() {
        List<Player> winners = Arrays.asList(player1, player2);
        Game game = new Game(1L, 1L, participants, script, "Test game", winners, "Test game name", List.of(player4));

        List<PlayerParticipation> newParticipants = Arrays.asList(
                new PlayerParticipation(player1, character1, true),
                new PlayerParticipation(player3, character3, true)
        );

        game.setParticipants(newParticipants);
        assertEquals(1, game.getWinningPlayers().size());
        assertTrue(game.getWinningPlayers().contains(player1));
        assertFalse(game.getWinningPlayers().contains(player2));
    }

    @Test
    void testParticipationWithNullPlayer() {
        List<PlayerParticipation> participationsWithNullPlayer = Arrays.asList(
                new PlayerParticipation(null, character1, true),
                new PlayerParticipation(player2, character2, true)
        );

        Game game =
                new Game(1L, 1L, participationsWithNullPlayer, script, Alignment.GOOD, "Test game", "Test game name",
                        List.of(player4));
        assertNotNull(game);
        assertEquals(2, game.getParticipants().size());
        assertNull(game.getParticipants().getFirst().getPlayer());
        assertEquals(player2, game.getParticipants().get(1).getPlayer());
    }

    @Test
    void testParticipationWithNullCharacter() {
        List<PlayerParticipation> participationsWithNullCharacter = Arrays.asList(
                new PlayerParticipation(player1, null, true),
                new PlayerParticipation(player2, character2, true)
        );

        Game game = new Game(1L, 1L, participationsWithNullCharacter, script, Alignment.GOOD, "Test game",
                "Test game name", List.of(player4));
        assertNotNull(game);
        assertEquals(2, game.getParticipants().size());
        assertNull(game.getParticipants().getFirst().getInitialCharacter());
        assertNull(game.getParticipants().getFirst().getInitialAlignment());
        assertNull(game.getParticipants().getFirst().getEndCharacter());
        assertNull(game.getParticipants().getFirst().getEndAlignment());
        assertEquals(character2, game.getParticipants().get(1).getInitialCharacter());
    }

    @Test
    void testParticipationWithNullPlayerAndCharacter() {
        List<PlayerParticipation> participationsWithNulls = Arrays.asList(
                new PlayerParticipation(null, null, true),
                new PlayerParticipation(player2, character2, true)
        );

        Game game = new Game(1L, 1L, participationsWithNulls, script, Alignment.GOOD, "Test game", "Test game name",
                List.of(player4));
        assertNotNull(game);
        assertEquals(2, game.getParticipants().size());
        assertNull(game.getParticipants().getFirst().getPlayer());
        assertNull(game.getParticipants().getFirst().getInitialCharacter());
        assertNull(game.getParticipants().getFirst().getInitialAlignment());
        assertNull(game.getParticipants().getFirst().getEndCharacter());
        assertNull(game.getParticipants().getFirst().getEndAlignment());
        assertEquals(player2, game.getParticipants().get(1).getPlayer());
        assertEquals(character2, game.getParticipants().get(1).getInitialCharacter());
    }

    @Test
    void testEqualsAndHashCode() {
        Game game1 =
                new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name", List.of(player4));
        Game game2 =
                new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name", List.of(player4));
        Game game3 =
                new Game(2L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name", List.of(player4));

        assertEquals(game1, game2);
        assertEquals(game1.hashCode(), game2.hashCode());
        assertNotEquals(game1, game3);
        assertNotEquals(game1.hashCode(), game3.hashCode());
    }

    @Test
    void testDuplicateStorytellers() {
        List<Player> duplicateStorytellers = Arrays.asList(player4, player4);
        assertThrows(IllegalArgumentException.class, () ->
                new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name",
                        duplicateStorytellers)
        );
    }

    @Test
    void testSetStorytellers() {
        Game game = new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name",
                List.of(player4));
        List<Player> newStorytellers = Arrays.asList(player1, player2);
        game.setStorytellers(newStorytellers);
        assertEquals(newStorytellers, game.getStorytellers());
    }

    @Test
    void testSetDuplicateStorytellers() {
        Game game = new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name",
                List.of(player4));
        List<Player> duplicateStorytellers = Arrays.asList(player1, player1);
        assertThrows(IllegalArgumentException.class, () -> game.setStorytellers(duplicateStorytellers));
    }

    @Test
    void testNullStorytellers() {
        Game game = new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name", null);
        assertNotNull(game.getStorytellers());
        assertTrue(game.getStorytellers().isEmpty());
    }

    @Test
    void testEmptyStorytellers() {
        Game game = new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name",
                new ArrayList<>());
        assertNotNull(game.getStorytellers());
        assertTrue(game.getStorytellers().isEmpty());
    }

    @Test
    void testMultipleStorytellers() {
        List<Player> storytellers = Arrays.asList(player1, player2, player4);
        Game game = new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name",
                storytellers);
        assertEquals(NUMBER_3, game.getStorytellers().size());
        assertTrue(game.getStorytellers().containsAll(storytellers));
    }

    @Test
    void testNullStorytellersEntry() {
        List<Player> storytellersWithNull = Arrays.asList(player1, null, player4);
        assertThrows(NullPointerException.class, () ->
                new Game(1L, 1L, participants, script, Alignment.GOOD, "Test game", "Test game name",
                        storytellersWithNull)
        );
    }

    @Test
    void testDescriptionTooLong() {
        String longDescription = "a".repeat(MAX_DESCRIPTION_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () ->
                new Game(1L, 1L, participants, script, Alignment.GOOD, longDescription, "Test game name",
                        List.of(player4))
        );
    }
}
