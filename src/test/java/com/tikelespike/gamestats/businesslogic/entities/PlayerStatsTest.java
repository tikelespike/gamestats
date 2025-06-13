package com.tikelespike.gamestats.businesslogic.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerStatsTest {
    private Player player;
    private PlayerStats playerStats;
    private Character townsfolkCharacter;
    private Character minionCharacter;
    private Script script;

    @BeforeEach
    void setUp() {
        player = new Player(1L, 0L, "TestPlayer", null);
        playerStats = new PlayerStats(player);

        // Create test characters
        townsfolkCharacter = new Character(1L, 0L, "Townsfolk", CharacterType.TOWNSFOLK);
        minionCharacter = new Character(2L, 0L, "Minion", CharacterType.MINION);

        // Create test script
        script = new Script(1L, 0L, "http://test", "Test Script", "Test Description", Set.of(townsfolkCharacter,
                minionCharacter));
    }

    @Test
    void addGameAsPlayerUpdatesBasicStats() {
        // Arrange
        PlayerParticipation participation = new PlayerParticipation(player, townsfolkCharacter, true);
        Game game = new Game(1L, 0L, java.util.List.of(participation), script, Alignment.GOOD,
                "Test game", "Test game name", java.util.List.of());

        // Act
        playerStats.addGame(game);

        // Assert
        assertEquals(1, playerStats.totalGamesPlayed());
        assertEquals(1, playerStats.totalWins());
        assertEquals(0, playerStats.timesStoryteller());
        assertEquals(0, playerStats.timesDeadAtEnd());
        assertEquals(1, playerStats.timesGood());
        assertEquals(0, playerStats.timesEvil());
    }

    @Test
    void addGameAsStorytellerUpdatesStorytellerCount() {
        // Arrange
        Game game = new Game(1L, 0L, java.util.List.of(), script, Alignment.GOOD,
                "Test game", "Test game name", java.util.List.of(player));

        // Act
        playerStats.addGame(game);

        // Assert
        assertEquals(1, playerStats.totalGamesPlayed());
        assertEquals(1, playerStats.timesStoryteller());
    }

    @Test
    void addGameUpdatesCharacterTypeCounts() {
        // Arrange
        PlayerParticipation participation = new PlayerParticipation(player, townsfolkCharacter, true);
        Game game = new Game(1L, 0L, java.util.List.of(participation), script, Alignment.GOOD,
                "Test game", "Test game name", java.util.List.of());

        // Act
        playerStats.addGame(game);

        // Assert
        Map<CharacterType, Integer> typeCounts = playerStats.characterTypeCounts();
        assertEquals(1, typeCounts.get(CharacterType.TOWNSFOLK));
        assertEquals(0, typeCounts.getOrDefault(CharacterType.MINION, 0));
    }

    @Test
    void addGameUpdatesCharacterPlayingCounts() {
        // Arrange
        PlayerParticipation participation = new PlayerParticipation(player, townsfolkCharacter, true);
        Game game = new Game(1L, 0L, java.util.List.of(participation), script, Alignment.GOOD,
                "Test game", "Test game name", java.util.List.of());

        // Act
        playerStats.addGame(game);

        // Assert
        Map<Character, Integer> playingCounts = playerStats.characterPlayingCounts();
        assertEquals(1, playingCounts.get(townsfolkCharacter));
    }

    @Test
    void addGameAsEvilPlayerUpdatesTeamStats() {
        // Arrange
        PlayerParticipation participation = new PlayerParticipation(player, minionCharacter, true);
        Game game = new Game(1L, 0L, java.util.List.of(participation), script, Alignment.EVIL,
                "Test game", "Test game name", java.util.List.of());

        // Act
        playerStats.addGame(game);

        // Assert
        assertEquals(1, playerStats.timesEvil());
        assertEquals(1, playerStats.totalWins());
    }

    @Test
    void addGameDeadAtEndUpdatesDeathCount() {
        // Arrange
        PlayerParticipation participation = new PlayerParticipation(player, townsfolkCharacter, false);
        Game game = new Game(1L, 0L, java.util.List.of(participation), script, Alignment.GOOD,
                "Test game", "Test game name", java.util.List.of());

        // Act
        playerStats.addGame(game);

        // Assert
        assertEquals(1, playerStats.timesDeadAtEnd());
    }

    @Test
    void addGameMultipleGamesAccumulatesStats() {
        // Arrange
        PlayerParticipation participation1 = new PlayerParticipation(player, townsfolkCharacter, true);
        Game game1 = new Game(1L, 0L, java.util.List.of(participation1), script, Alignment.GOOD,
                "Test game 1", "Test game name 1", java.util.List.of());

        PlayerParticipation participation2 = new PlayerParticipation(player, minionCharacter, true);
        Game game2 = new Game(2L, 0L, java.util.List.of(participation2), script, Alignment.EVIL,
                "Test game 2", "Test game name 2", java.util.List.of());

        // Act
        playerStats.addGame(game1);
        playerStats.addGame(game2);

        // Assert
        assertEquals(2, playerStats.totalGamesPlayed());
        assertEquals(2, playerStats.totalWins());
        assertEquals(1, playerStats.timesGood());
        assertEquals(1, playerStats.timesEvil());
        Map<Character, Integer> playingCounts = playerStats.characterPlayingCounts();
        assertEquals(1, playingCounts.get(townsfolkCharacter));
        assertEquals(1, playingCounts.get(minionCharacter));
    }
}
