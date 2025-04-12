package com.tikelespike.gamestats.businesslogic.entities;

import java.util.List;

/**
 * A single game of Blood on the Clocktower.
 */
public interface Game extends HasId, HasVersion {

    /**
     * Returns a map of all players that participated in this game and the data about their participation in this game.
     * This includes the character they played, their alignment, and other statistical data.
     *
     * @return a map mapping from players to data about the player's participation in this game
     */
    List<PlayerParticipation> getParticipants();

    /**
     * Returns the script (that is, the list of characters that may appear in this game) that was used in this game.
     *
     * @return the script used in this game
     */
    Script getScript();

    /**
     * Returns the alignment that won this game. If null, use {@link #getWinningPlayers()} to determine the winning
     * players (there may be instances where not a single alignment wins, but the situation is more complex or a draw).
     *
     * @return the alignment that won this game
     */
    Alignment getWinningAlignment();

    /**
     * Returns a free-form optional description of this game, which may include some notes about how the game went,
     * strategies of players, etc.
     *
     * @return a free-form optional description of this game
     */
    String getDescription();

    /**
     * Returns the players that won this game. By default, these are the players whose alignment matches the winning
     * alignment. In more complex scenarios, this method may be overridden.
     *
     * @return the players that won this game
     */
    default List<Player> getWinningPlayers() {
        return getParticipants().stream()
                .filter(entry -> entry.endAlignment() == getWinningAlignment())
                .map(PlayerParticipation::player)
                .toList();
    }
}
