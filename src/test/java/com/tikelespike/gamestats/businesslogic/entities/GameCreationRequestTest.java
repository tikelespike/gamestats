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

    private static final long NUMBER_3 = 3L; // crude checkstyle magic number bypass
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

        participants = List.of(
                new PlayerParticipation(player1, character1, true),
                new PlayerParticipation(player2, character2, true),
                new PlayerParticipation(player3, character3, true)
        );
    }

    @Test
    void testCreateWithWinningAlignment() {
        GameCreationRequest request =
                new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", null, "Test game name",
                        List.of(player4));
        assertNotNull(request);
        assertEquals(script, request.script());
        assertEquals(participants, request.participants());
        assertEquals(Alignment.GOOD, request.winningAlignment());
        assertEquals("Test game", request.description());
        assertNull(request.winningPlayers());
    }

    @Test
    void testCreateWithWinningPlayers() {
        List<Player> winners = List.of(player1, player3);
        GameCreationRequest request =
                new GameCreationRequest(script, participants, null, "Test game", winners, "Test game name",
                        List.of(player4));
        assertNotNull(request);
        assertEquals(script, request.script());
        assertEquals(participants, request.participants());
        assertNull(request.winningAlignment());
        assertEquals("Test game", request.description());
        assertEquals(winners, request.winningPlayers());
    }

    @Test
    void testNullScript() {
        GameCreationRequest gameCreationRequest =
                new GameCreationRequest(null, participants, Alignment.GOOD, "Test game", null, "Test game name",
                        List.of(player4));
        assertNull(gameCreationRequest.script());
    }

    @Test
    void testDuplicateParticipants() {
        List<PlayerParticipation> duplicateParticipants = new ArrayList<>(participants);
        duplicateParticipants.add(new PlayerParticipation(player1, character2, true));

        assertThrows(IllegalArgumentException.class, () ->
                new GameCreationRequest(script, duplicateParticipants, Alignment.GOOD, "Test game", null,
                        "Test game name", List.of(player4))
        );
    }

    @Test
    void testWinningPlayersNotInGame() {
        Player outsidePlayer = new Player("OutsidePlayer");
        List<Player> invalidWinners = List.of(player1, outsidePlayer);

        assertThrows(IllegalArgumentException.class, () ->
                new GameCreationRequest(script, participants, null, "Test game", invalidWinners, "Test game name",
                        List.of(player4))
        );
    }

    @Test
    void testNullWinningPlayersWhenAlignmentIsNull() {
        assertThrows(NullPointerException.class, () ->
                new GameCreationRequest(script, participants, null, "Test game", null, "Test game name",
                        List.of(player4))
        );
    }

    @Test
    void testWinningPlayersIgnoredWhenAlignmentSet() {
        List<Player> winners = List.of(player1, player2);
        GameCreationRequest request =
                new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", winners, "Test game name",
                        List.of(player4));
        assertNotNull(request);
        assertEquals(Alignment.GOOD, request.winningAlignment());
        assertNull(request.winningPlayers());
    }

    @Test
    void testParticipationWithNullPlayer() {
        List<PlayerParticipation> participationsWithNullPlayer = List.of(
                new PlayerParticipation(null, character1, true),
                new PlayerParticipation(player2, character2, true)
        );

        GameCreationRequest request = new GameCreationRequest(script, participationsWithNullPlayer, Alignment.GOOD,
                "Test game", null, "Test game name", List.of(player4));
        assertNotNull(request);
        assertEquals(2, request.participants().size());
        assertNull(request.participants().getFirst().getPlayer());
        assertEquals(player2, request.participants().get(1).getPlayer());
    }

    @Test
    void testParticipationWithNullCharacter() {
        List<PlayerParticipation> participationsWithNullCharacter = List.of(
                new PlayerParticipation(player1, null, true),
                new PlayerParticipation(player2, character2, true)
        );

        GameCreationRequest request = new GameCreationRequest(script, participationsWithNullCharacter, Alignment.GOOD,
                "Test game", null, "Test game name", List.of(player4));
        assertNotNull(request);
        assertEquals(2, request.participants().size());
        assertNull(request.participants().getFirst().getInitialCharacter());
        assertNull(request.participants().getFirst().getInitialAlignment());
        assertNull(request.participants().getFirst().getEndCharacter());
        assertNull(request.participants().getFirst().getEndAlignment());
        assertEquals(character2, request.participants().get(1).getInitialCharacter());
    }

    @Test
    void testParticipationWithNullPlayerAndCharacter() {
        List<PlayerParticipation> participationsWithNulls = List.of(
                new PlayerParticipation(null, null, true),
                new PlayerParticipation(player2, character2, true)
        );

        GameCreationRequest request = new GameCreationRequest(script, participationsWithNulls, Alignment.GOOD,
                "Test game", null, "Test game name", List.of(player4));
        assertNotNull(request);
        assertEquals(2, request.participants().size());
        assertNull(request.participants().getFirst().getPlayer());
        assertNull(request.participants().getFirst().getInitialCharacter());
        assertNull(request.participants().getFirst().getInitialAlignment());
        assertNull(request.participants().getFirst().getEndCharacter());
        assertNull(request.participants().getFirst().getEndAlignment());
        assertEquals(player2, request.participants().get(1).getPlayer());
        assertEquals(character2, request.participants().get(1).getInitialCharacter());
    }

    @Test
    void testDuplicateStorytellers() {
        List<Player> duplicateStorytellers = Arrays.asList(player4, player4);
        assertThrows(IllegalArgumentException.class, () ->
                new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", null, "Test game name",
                        duplicateStorytellers)
        );
    }

    @Test
    void testNullStorytellers() {
        GameCreationRequest request = new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", null,
                "Test game name", null);
        assertNotNull(request.storytellers());
        assertEquals(0, request.storytellers().size());
    }

    @Test
    void testEmptyStorytellers() {
        GameCreationRequest request = new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", null,
                "Test game name", new ArrayList<>());
        assertNotNull(request.storytellers());
        assertEquals(0, request.storytellers().size());
    }

    @Test
    void testMultipleStorytellers() {
        List<Player> storytellers = Arrays.asList(player1, player2, player4);
        GameCreationRequest request = new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", null,
                "Test game name", storytellers);
        assertEquals(NUMBER_3, request.storytellers().size());
        assertEquals(storytellers, request.storytellers());
    }

    @Test
    void testNullStorytellersEntry() {
        List<Player> storytellersWithNull = Arrays.asList(player1, null, player4);
        assertThrows(NullPointerException.class, () ->
                new GameCreationRequest(script, participants, Alignment.GOOD, "Test game", null, "Test game name",
                        storytellersWithNull)
        );
    }
}
