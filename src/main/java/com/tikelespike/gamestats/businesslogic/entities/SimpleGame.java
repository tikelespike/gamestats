package com.tikelespike.gamestats.businesslogic.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link Game} interface. Assumes that the winners of a game are always either the good
 * aligned or the evil aligned players.
 */
public class SimpleGame implements Game {

    private final Long id;
    private final Map<Player, PlayerParticipation> participants;
    private final Script script;
    private final Alignment winningAlignment;
    private final String description;

    /**
     * Creates a new game with the given data.
     *
     * @param id unique identifier of this game
     * @param participants map containing statistical data for all players participating in this game
     * @param script the script (list of available characters) used in the game
     * @param winningAlignment the alignment that won the game
     * @param description a free-form optional description of this game
     */
    public SimpleGame(Long id, Map<Player, PlayerParticipation> participants, Script script, Alignment winningAlignment,
                      String description) {
        this.id = id;
        this.participants = participants;
        this.script = script;
        this.winningAlignment = winningAlignment;
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Map<Player, PlayerParticipation> getParticipants() {
        return new HashMap<>(participants);
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
}
