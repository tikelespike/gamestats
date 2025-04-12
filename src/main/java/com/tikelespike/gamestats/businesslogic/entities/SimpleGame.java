package com.tikelespike.gamestats.businesslogic.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the {@link Game} interface.
 */
public class SimpleGame implements Game {

    private final Long id;
    private final Long version;
    private final List<PlayerParticipation> participants;
    private final Script script;
    private final Alignment winningAlignment;
    private final String description;
    private final List<Player> winningPlayers;

    /**
     * Creates a new game with the given data, assuming that the game was won by either all good-aligned or all
     * evil-aligned players.
     *
     * @param id unique identifier of this game
     * @param version version counter for optimistic locking
     * @param participants map containing statistical data for all players participating in this game
     * @param script the script (list of available characters) used in the game
     * @param winningAlignment the alignment that won the game
     * @param description a free-form optional description of this game
     */
    public SimpleGame(Long id, Long version, List<PlayerParticipation> participants, Script script,
                      Alignment winningAlignment,
                      String description) {
        this.id = id;
        this.version = version;
        this.participants = participants;
        this.script = script;
        this.winningAlignment = winningAlignment;
        this.description = description;
        this.winningPlayers = null;
    }

    /**
     * Creates a new game with the given data, assuming that the winning team is not defined by its alignment, but by a
     * more complex situation (for example due to characters like the politician which potentially wins the game alone).
     * If the winning players are simply all players of one of the two alignments in the game, use
     * {@link #SimpleGame(Long, Long, List, Script, Alignment, String)} instead.
     *
     * @param id unique identifier of this game
     * @param version version counter for optimistic locking
     * @param participants map containing statistical data for all players participating in this game
     * @param script the script (list of available characters) used in the game
     * @param description a free-form optional description of this game
     * @param winningPlayers a list of all players that won this game
     */
    public SimpleGame(Long id, Long version, List<PlayerParticipation> participants, Script script, String description,
                      List<Player> winningPlayers) {
        this.id = id;
        this.version = version;
        this.participants = participants;
        this.script = script;
        this.winningAlignment = null;
        this.description = description;
        this.winningPlayers = winningPlayers;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public List<PlayerParticipation> getParticipants() {
        return new ArrayList<>(participants);
    }

    @Override
    public Script getScript() {
        return script;
    }

    @Override
    public Alignment getWinningAlignment() {
        return winningAlignment;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Player> getWinningPlayers() {
        if (winningAlignment != null) {
            return Game.super.getWinningPlayers();
        }
        return winningPlayers;
    }
}
