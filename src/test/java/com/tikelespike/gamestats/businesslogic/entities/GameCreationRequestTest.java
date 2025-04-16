package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameCreationRequestTest {

    private static final long ID_3 = 3L;
    private Player player1;
    private Player player2;
    private Player player3;
    private Script script;
    private List<PlayerParticipation> participants;
    private Character character2;

    @BeforeEach
    void setUp() {
        player1 = new Player(0L, 0L, "Player1", null);
        player2 = new Player(1L, 0L, "Player2", null);
        player3 = new Player(2L, 0L, "Player3", null);

        Character character1 = new Character(1L, 1L, "Character1", CharacterType.TOWNSFOLK);
        character2 = new Character(2L, 1L, "Character2", CharacterType.MINION);
        Character character3 = new Character(ID_3, 1L, "Character3", CharacterType.TOWNSFOLK);

        script = new Script(1L, 1L, null, "Test Script", "Test Description",
                new HashSet<>(Arrays.asList(character1, character2, character3)));

        participants = Arrays.asList(
                new PlayerParticipation(player1, character1, true),
                new PlayerParticipation(player2, character2, true),
                new PlayerParticipation(player3, character3, true)
        );
    }

    @Test
    void testCreateWithWinningAlignment() {
        GameCreationRequest request =
                new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", null, "Test game name");
        assertNotNull(request);
        assertEquals(script, request.script());
        assertEquals(participants, request.participants());
        assertEquals(Alignment.GOOD, request.winningAlignment());
        assertEquals("Test game", request.description());
        assertNull(request.winningPlayers());
    }

    @Test
    void testCreateWithWinningPlayers() {
        List<Player> winners = Arrays.asList(player1, player3);
        GameCreationRequest request =
                new GameCreationRequest(script, participants, null, "Test game", winners, "Test game name");
        assertNotNull(request);
        assertEquals(script, request.script());
        assertEquals(participants, request.participants());
        assertNull(request.winningAlignment());
        assertEquals("Test game", request.description());
        assertEquals(winners, request.winningPlayers());
    }

    @Test
    void testNullScript() {
        assertThrows(NullPointerException.class, () ->
                new GameCreationRequest(null, participants, Alignment.GOOD, "Test game", null, "Test game name")
        );
    }

    @Test
    void testDuplicateParticipants() {
        List<PlayerParticipation> duplicateParticipants = new ArrayList<>(participants);
        duplicateParticipants.add(new PlayerParticipation(player1, character2, true));

        assertThrows(IllegalArgumentException.class, () ->
                new GameCreationRequest(script, duplicateParticipants, Alignment.GOOD, "Test game", null,
                        "Test game name")
        );
    }

    @Test
    void testWinningPlayersNotInGame() {
        Player outsidePlayer = new Player("OutsidePlayer");
        List<Player> invalidWinners = Arrays.asList(player1, outsidePlayer);

        assertThrows(IllegalArgumentException.class, () ->
                new GameCreationRequest(script, participants, null, "Test game", invalidWinners, "Test game name")
        );
    }

    @Test
    void testNullWinningPlayersWhenAlignmentIsNull() {
        assertThrows(NullPointerException.class, () ->
                new GameCreationRequest(script, participants, null, "Test game", null, "Test game name")
        );
    }

    @Test
    void testWinningPlayersIgnoredWhenAlignmentSet() {
        List<Player> winners = Arrays.asList(player1, player2);
        GameCreationRequest request =
                new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", winners, "Test game name");
        assertNotNull(request);
        assertEquals(Alignment.GOOD, request.winningAlignment());
        assertNull(request.winningPlayers());
    }
}
