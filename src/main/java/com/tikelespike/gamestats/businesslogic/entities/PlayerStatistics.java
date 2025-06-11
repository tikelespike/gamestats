package com.tikelespike.gamestats.businesslogic.entities;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Statistics about a player, like the number of games won.
 */
public final class PlayerStatistics {
    private final Player player;
    private int totalGamesPlayed;
    private int totalWins;
    private int timesStoryteller;
    private int timesDeadAtEnd;
    private int timesGood;
    private int timesEvil;
    private final Map<CharacterType, Integer> characterTypeCounts = new EnumMap<>(CharacterType.class);
    private final Map<Character, Integer> characterPlayingCounts = new HashMap<>();

    /**
     * Creates a new player statistics object.
     *
     * @param player the player this statistics are about (may not be null)
     */
    public PlayerStatistics(Player player) {
        this.player = Objects.requireNonNull(player);
    }

    /**
     * Updates these statistics with the data from a new game not yet tracked by this object.
     *
     * @param game the game to add to these statistics (may not be null)
     */
    public void addGame(Game game) {
        Objects.requireNonNull(game, "Game must not be null");

        if (game.getStorytellers().contains(player)) {
            timesStoryteller++;
            totalGamesPlayed++;
            return;
        }

        Optional<PlayerParticipation> optParticipation = game.getParticipants().stream()
                .filter(p -> player.equals(p.getPlayer()))
                .findFirst();

        if (optParticipation.isEmpty()) {
            return;
        }

        PlayerParticipation participation = optParticipation.get();

        totalGamesPlayed++;

        if (game.getWinningPlayers().contains(player)) {
            totalWins++;
        }

        if (!participation.getIsAliveAtEnd()) {
            timesDeadAtEnd++;
        }

        if (participation.getEndAlignment() == Alignment.GOOD) {
            timesGood++;
        } else if (participation.getEndAlignment() == Alignment.EVIL) {
            timesEvil++;
        }

        Character startCharacter = participation.getInitialCharacter();
        Character endCharacter = participation.getEndCharacter();
        if (startCharacter != null) {
            characterPlayingCounts.merge(startCharacter, 1, Integer::sum);
        }
        if (endCharacter != null && !endCharacter.equals(startCharacter)) {
            characterPlayingCounts.merge(endCharacter, 1, Integer::sum);
        }

        CharacterType startCharacterType = startCharacter != null ? startCharacter.getCharacterType() : null;
        CharacterType endCharacterType = endCharacter != null ? endCharacter.getCharacterType() : null;

        if (startCharacterType != null) {
            characterTypeCounts.merge(startCharacterType, 1, Integer::sum);
        }
        if (endCharacterType != null && !endCharacterType.equals(startCharacterType)) {
            characterTypeCounts.merge(endCharacterType, 1, Integer::sum);
        }
    }

    /**
     * @return the player this statistics are about (not null)
     */
    public Player player() {
        return player;
    }

    /**
     * @return the total number of games this player has played, either as a player or as a storyteller
     */
    public int totalGamesPlayed() {
        return totalGamesPlayed;
    }

    /**
     * @return the total number of games this player has won
     */
    public int totalWins() {
        return totalWins;
    }

    /**
     * @return the number of games this player has acted as storyteller for
     */
    public int timesStoryteller() {
        return timesStoryteller;
    }

    /**
     * @return the number of games where the player was dead at the end of the game
     */
    public int timesDeadAtEnd() {
        return timesDeadAtEnd;
    }

    /**
     * @return the number of games where the player was on the good team at the end
     */
    public int timesGood() {
        return timesGood;
    }

    /**
     * @return the number of games where the player was on the evil team at the end
     */
    public int timesEvil() {
        return timesEvil;
    }

    /**
     * @return a map mapping each character type to the number of times this player has played a character of that type
     */
    public Map<CharacterType, Integer> characterTypeCounts() {
        return characterTypeCounts;
    }

    /**
     * @return a map mapping each character to the number of games this player has played that character
     */
    public Map<Character, Integer> characterPlayingCounts() {
        return characterPlayingCounts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (PlayerStatistics) obj;
        return Objects.equals(this.player, that.player)
                && this.totalGamesPlayed == that.totalGamesPlayed
                && this.totalWins == that.totalWins
                && this.timesStoryteller == that.timesStoryteller
                && this.timesDeadAtEnd == that.timesDeadAtEnd
                && this.timesGood == that.timesGood
                && this.timesEvil == that.timesEvil
                && Objects.equals(this.characterTypeCounts, that.characterTypeCounts)
                && Objects.equals(this.characterPlayingCounts, that.characterPlayingCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, totalGamesPlayed, totalWins, timesStoryteller, timesDeadAtEnd, timesGood, timesEvil,
                characterTypeCounts, characterPlayingCounts);
    }

    @Override
    public String toString() {
        return "PlayerStatistics["
                + "player=" + player + ", "
                + "totalGamesPlayed=" + totalGamesPlayed + ", "
                + "totalWins=" + totalWins + ", "
                + "timesStoryteller=" + timesStoryteller + ", "
                + "timesDeadAtEnd=" + timesDeadAtEnd + ", "
                + "timesGood=" + timesGood + ", "
                + "timesEvil=" + timesEvil + ", "
                + "characterTypeCounts=" + characterTypeCounts + ", "
                + "characterPlayingCounts=" + characterPlayingCounts + ']';
    }

}
