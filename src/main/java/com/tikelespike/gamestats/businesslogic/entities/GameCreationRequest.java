package com.tikelespike.gamestats.businesslogic.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


/**
 * Request object for creating a new game. Contains all information required to create a game.
 *
 * @param script the script (list of available characters) used in the game (may not be null)
 * @param participants list containing players and their game-specific data (may not contain the same player
 *         twice or be null)
 * @param winningAlignment the alignment that won the game
 * @param description a free-form optional description of this game
 * @param winningPlayers a list of all players that won this game (may not contain non-participating players)
 * @param name human-readable name of this game (may not be null)
 */
public record GameCreationRequest(
        Script script,
        List<PlayerParticipation> participants,
        Alignment winningAlignment,
        String description,
        List<Player> winningPlayers,
        String name
) {
    /**
     * Creates a new game creation request.
     *
     * @param script the script (list of available characters) used in the game (may not be null)
     * @param participants list containing players and their game-specific data (may not contain the same player
     *         twice or be null)
     * @param winningAlignment the alignment that won the game
     * @param description a free-form optional description of this game
     * @param winningPlayers a list of all players that won this game (may not contain non-participating
     *         players, may not be null if winningAlignment is null, is ignored otherwise)
     * @param name human-readable name of this game (may not be null)
     */
    public GameCreationRequest(Script script, List<PlayerParticipation> participants, Alignment winningAlignment,
                               String description, List<Player> winningPlayers, String name) {
        this.script = Objects.requireNonNull(script);
        this.participants = participants;
        List<Long> playerIds = participants.stream().map(participation -> participation.player().getId()).toList();
        if (containsDuplicates(playerIds)) {
            throw new IllegalArgumentException("The same player cannot participate multiple times in the same game.");
        }
        this.winningAlignment = winningAlignment;
        this.description = description;
        this.winningPlayers = winningAlignment == null ? Objects.requireNonNull(winningPlayers) : null;
        if (this.winningPlayers != null && winningPlayers.stream()
                .anyMatch(p -> !playerIds.contains(p.getId()))) {
            throw new IllegalArgumentException("A player that did not participate cannot win the game");
        }
        this.name = Objects.requireNonNull(name, "Game name may not be null");
    }

    private <T> boolean containsDuplicates(Collection<T> collection) {
        return new HashSet<>(collection).size() != collection.size();
    }
}
