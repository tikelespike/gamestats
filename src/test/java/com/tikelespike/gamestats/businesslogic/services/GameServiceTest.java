package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.GamestatsApplication;
import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.GameCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.entities.ScriptCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GamestatsApplication.class)
class GameServiceTest {

    private static final long NON_EXISTENT_ID = 12345L;

    @Autowired
    private GameService gameService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private PlayerService playerService;

    @Test
    void testCreateGameWithWinningAlignment() {
        // Setup
        Script script = addTestScript("testCreateGameWithWinningAlignment");
        Player player = addTestPlayer("testCreateGameWithWinningAlignment");
        Character character = addTestCharacter("testCreateGameWithWinningAlignment");

        PlayerParticipation participation = new PlayerParticipation(
                player,
                character,
                true
        );

        GameCreationRequest request = new GameCreationRequest(
                script,
                List.of(participation),
                Alignment.GOOD,
                "Test game description",
                null
        );

        // Execute
        Game createdGame = gameService.createGame(request);

        // Verify
        assertNotNull(createdGame);
        assertEquals(script, createdGame.getScript());
        assertEquals(Alignment.GOOD, createdGame.getWinningAlignment());
        assertEquals("Test game description", createdGame.getDescription());
        assertEquals(1, createdGame.getParticipants().size());
        assertEquals(player, createdGame.getParticipants().getFirst().player());
        assertEquals(character, createdGame.getParticipants().getFirst().initialCharacter());
        assertEquals(Alignment.GOOD, createdGame.getParticipants().getFirst().initialAlignment());
        assertEquals(1, createdGame.getWinningPlayers().size());
    }

    @Test
    void testCreateGameWithWinningPlayers() {
        // Setup
        Script script = addTestScript("testCreateGameWithWinningPlayers");
        Player player1 = addTestPlayer("testCreateGameWithWinningPlayers_1");
        Player player2 = addTestPlayer("testCreateGameWithWinningPlayers_2");
        Character character = addTestCharacter("testCreateGameWithWinningPlayers");

        PlayerParticipation participation1 = new PlayerParticipation(
                player1,
                character,
                true
        );
        PlayerParticipation participation2 = new PlayerParticipation(
                player2,
                character,
                Alignment.EVIL,
                character,
                Alignment.EVIL,
                true
        );

        GameCreationRequest request = new GameCreationRequest(
                script,
                List.of(participation1, participation2),
                null,
                "Test game description",
                List.of(player1)
        );

        // Execute
        Game createdGame = gameService.createGame(request);

        // Verify
        assertNotNull(createdGame);
        assertEquals(script, createdGame.getScript());
        assertNull(createdGame.getWinningAlignment());
        assertEquals("Test game description", createdGame.getDescription());
        assertEquals(2, createdGame.getParticipants().size());
        assertEquals(1, createdGame.getWinningPlayers().size());
        assertEquals(player1, createdGame.getWinningPlayers().getFirst());
    }

    @Test
    void testCreateGameNullRequest() {
        assertThrows(NullPointerException.class, () -> gameService.createGame(null));
    }

    @Test
    void testCreateGameWithNonExistentScript() {
        // Setup
        Script nonExistentScript =
                new Script(NON_EXISTENT_ID, 1L, "Non-existent", "Description", "http://test", Set.of());
        Player player = addTestPlayer("testCreateGameWithNonExistentScript");
        Character character = addTestCharacter("testCreateGameWithNonExistentScript");

        PlayerParticipation participation = new PlayerParticipation(
                player,
                character,
                true
        );

        GameCreationRequest request = new GameCreationRequest(
                nonExistentScript,
                List.of(participation),
                Alignment.GOOD,
                "Test game description",
                null
        );

        // Execute & Verify
        assertThrows(RelatedResourceNotFoundException.class, () -> gameService.createGame(request));
    }

    @Test
    void testCreateGameWithNonExistentCharacter() {
        // Setup
        Script script = addTestScript("testCreateGameWithNonExistentCharacter");
        Player player = addTestPlayer("testCreateGameWithNonExistentCharacter");
        Character nonExistentCharacter = new Character(NON_EXISTENT_ID, 1L, "Non-existent", "Non-existent",
                CharacterType.TOWNSFOLK, "http://test", "http://test/image");

        PlayerParticipation participation = new PlayerParticipation(
                player,
                nonExistentCharacter,
                true
        );

        GameCreationRequest request = new GameCreationRequest(
                script,
                List.of(participation),
                Alignment.GOOD,
                "Test game description",
                null
        );

        // Execute & Verify
        assertThrows(RelatedResourceNotFoundException.class, () -> gameService.createGame(request));
    }

    @Test
    void testCreateGameWithNonExistentPlayer() {
        // Setup
        Script script = addTestScript("testCreateGameWithNonExistentPlayer");
        Player nonExistentPlayer = new Player(NON_EXISTENT_ID, 1L, "Non-existent", null);
        Character character = addTestCharacter("testCreateGameWithNonExistentPlayer");

        PlayerParticipation participation = new PlayerParticipation(
                nonExistentPlayer,
                character,
                true
        );

        GameCreationRequest request = new GameCreationRequest(
                script,
                List.of(participation),
                Alignment.GOOD,
                "Test game description",
                null
        );

        // Execute & Verify
        assertThrows(RelatedResourceNotFoundException.class, () -> gameService.createGame(request));
    }

    @Test
    void testGetGame() {
        // Setup
        Game game = addTestGame("testGetGame");

        // Execute
        Game retrievedGame = gameService.getGame(game.getId());

        // Verify
        assertNotNull(retrievedGame);
        assertEquals(game, retrievedGame);
    }

    @Test
    void testGetGameNonExistent() {
        Game retrievedGame = gameService.getGame(NON_EXISTENT_ID);
        assertNull(retrievedGame);
    }

    @Test
    void testGetAllGames() {
        // Setup
        Game game = addTestGame("testGetAllGames");

        // Execute
        List<Game> games = gameService.getAllGames();

        // Verify
        assertNotNull(games);
        assertTrue(games.contains(game));
    }

    @Test
    void testUpdateGame() throws StaleDataException {
        // Setup
        Game game = addTestGame("testUpdateGame");
        game.setDescription("Updated description");

        // Execute
        Game updatedGame = gameService.updateGame(game);

        // Verify
        assertNotNull(updatedGame);
        assertEquals(game, updatedGame);
        assertEquals("Updated description", updatedGame.getDescription());
    }

    @Test
    void testUpdateGameNull() {
        assertThrows(NullPointerException.class, () -> gameService.updateGame(null));
    }

    @Test
    void testUpdateGameNonExistent() {
        Game nonExistentGame = new Game(NON_EXISTENT_ID, 1L, List.of(), null, Alignment.GOOD, "Test");
        assertThrows(ResourceNotFoundException.class, () -> gameService.updateGame(nonExistentGame));
    }

    @Test
    void testUpdateGameStaleData() throws StaleDataException {
        // Setup
        Game game = addTestGame("testUpdateGameStaleData");
        game.setDescription("First update");
        gameService.updateGame(game);
        game.setDescription("Second update");

        // Execute & Verify
        assertThrows(StaleDataException.class, () -> gameService.updateGame(game));
    }

    @Test
    void testDeleteGame() {
        // Setup
        Game game = addTestGame("testDeleteGame");

        // Execute
        gameService.deleteGame(game.getId());

        // Verify
        assertNull(gameService.getGame(game.getId()));
    }

    @Test
    void testDeleteGameNonExistent() {
        // Should not throw an exception
        gameService.deleteGame(NON_EXISTENT_ID);
    }

    private Game addTestGame(String testName) {
        Script script = addTestScript(testName);
        Player player = addTestPlayer(testName);
        Character character = addTestCharacter(testName);

        PlayerParticipation participation = new PlayerParticipation(
                player,
                character,
                true
        );

        GameCreationRequest request = new GameCreationRequest(
                script,
                List.of(participation),
                Alignment.GOOD,
                "Test game description for " + testName,
                null
        );

        return gameService.createGame(request);
    }

    private Script addTestScript(String testName) {
        Character character = addTestCharacter(testName);
        ScriptCreationRequest request = new ScriptCreationRequest(
                testName + "_name",
                testName + "_description",
                "http://" + testName,
                Set.of(character)
        );
        return scriptService.createScript(request);
    }

    private Character addTestCharacter(String testName) {
        CharacterCreationRequest request = new CharacterCreationRequest(
                testName + "_id",
                testName + "_name",
                CharacterType.TOWNSFOLK,
                "http://" + testName,
                "http://" + testName + "/image"
        );
        return characterService.createCharacter(request);
    }

    private Player addTestPlayer(String testName) {
        return playerService.createPlayer(testName + "_name");
    }
}
