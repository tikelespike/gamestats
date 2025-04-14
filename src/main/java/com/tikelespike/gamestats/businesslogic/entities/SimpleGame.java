package com.tikelespike.gamestats.businesslogic.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
     * @param participants list containing players and their game-specific data (may not contain the same player
     *         twice or be null)
     * @param script the script (list of available characters) used in the game (may not be null)
     * @param winningAlignment the alignment that won the game (may not be null)
     * @param description a free-form optional description of this game
     */
    public SimpleGame(Long id, Long version, List<PlayerParticipation> participants, Script script,
                      Alignment winningAlignment,
                      String description) {
        this.id = id;
        this.version = version;
        this.participants = participants;
        List<Long> playerIds = participants.stream().map(participation -> participation.player().getId()).toList();
        if (containsDuplicates(playerIds)) {
            throw new IllegalArgumentException("The same player cannot participate multiple times in the same game.");
        }
        this.script = Objects.requireNonNull(script);
        this.winningAlignment = Objects.requireNonNull(winningAlignment);
        this.description = description;
        this.winningPlayers = null;
    }

    private <T> boolean containsDuplicates(Collection<T> collection) {
        return new HashSet<>(collection).size() != collection.size();
    }

    /**
     * Creates a new game with the given data, assuming that the winning team is not defined by its alignment, but by a
     * more complex situation (for example due to characters like the politician which potentially wins the game alone).
     * If the winning players are simply all players of one of the two alignments in the game, use
     * {@link #SimpleGame(Long, Long, List, Script, Alignment, String)} instead.
     *
     * @param id unique identifier of this game
     * @param version version counter for optimistic locking
     * @param participants list containing players and their game-specific data (may not contain the same player
     *         twice)
     * @param script the script (list of available characters) used in the game (may not be null)
     * @param description a free-form optional description of this game
     * @param winningPlayers a list of all players that won this game (may not be null and may not contain
     *         non-participating players)
     */
    public SimpleGame(Long id, Long version, List<PlayerParticipation> participants, Script script, String description,
                      List<Player> winningPlayers) {
        this.id = id;
        this.version = version;
        this.participants = participants;
        List<Long> playerIds = participants.stream().map(participation -> participation.player().getId()).toList();
        if (containsDuplicates(playerIds)) {
            throw new IllegalArgumentException("The same player cannot participate multiple times in the same game.");
        }
        this.script = Objects.requireNonNull(script);
        this.winningAlignment = null;
        this.description = description;
        this.winningPlayers = Objects.requireNonNull(winningPlayers);
        if (winningPlayers.stream()
                .anyMatch(p -> !playerIds.contains(p.getId()))) {
            throw new IllegalArgumentException("A player that did not participate cannot win the game");
        }
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
