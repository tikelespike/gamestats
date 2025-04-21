package com.tikelespike.gamestats.businesslogic.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of the {@link Game} interface. Has data about the players participating in the game, like the
 * characters they played, the team they were in and if they won the game, as well as meta information like a
 * description and the script which was played.
 */
public class Game implements HasId, HasVersion {

    private final Long id;
    private final Long version;
    private List<PlayerParticipation> participants;
    private Script script;
    private Alignment winningAlignment;
    private String description;
    private List<Player> winningPlayers;
    private String name;

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
     * @param name human-readable name of this game (may not be null)
     */
    public Game(Long id, Long version, List<PlayerParticipation> participants, Script script,
                Alignment winningAlignment, String description, String name) {
        this.id = id;
        this.version = version;
        setParticipants(participants);
        setScript(script);
        setWinningAlignment(winningAlignment);
        setDescription(description);
        setName(name);
    }

    /**
     * Creates a new game with the given data, assuming that the winning team is not defined by its alignment, but by a
     * more complex situation (for example due to characters like the politician which potentially wins the game alone).
     * If the winning players are simply all players of one of the two alignments in the game, use
     * {@link #Game(Long, Long, List, Script, Alignment, String, String)} instead.
     *
     * @param id unique identifier of this game
     * @param version version counter for optimistic locking
     * @param participants list containing players and their game-specific data (may not contain the same player
     *         twice or be null)
     * @param script the script (list of available characters) used in the game (may not be null)
     * @param description a free-form optional description of this game
     * @param winningPlayers a list of all players that won this game (may not be null and may not contain
     *         non-participating players)
     * @param name human-readable name of this game (may not be null)
     */
    public Game(Long id, Long version, List<PlayerParticipation> participants, Script script, String description,
                List<Player> winningPlayers, String name) {
        this.id = id;
        this.version = version;
        setParticipants(participants);
        setScript(script);
        setDescription(description);
        setWinningPlayers(winningPlayers);
        setName(name);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    /**
     * Returns a list of all players that participated in this game and the data about their participation in this game.
     * This includes the character they played, their alignment, and other statistical data.
     *
     * @return a list of all players and their game-related data
     */
    public List<PlayerParticipation> getParticipants() {
        return new ArrayList<>(participants);
    }

    /**
     * Sets the list of all players that participated in this game, along with the game-specific data about them (e.g .
     * which character they played).
     *
     * @param participants the list of all players and their game-related data. May not be null or contain the
     *         same player multiple times.
     */
    public void setParticipants(List<PlayerParticipation> participants) {
        List<Long> playerIds = participants.stream()
                .map(participation -> participation.getPlayer())
                .filter(Objects::nonNull)
                .map(Player::getId)
                .toList();
        if (containsDuplicates(playerIds)) {
            throw new IllegalArgumentException("The same player cannot participate multiple times in the same game.");
        }

        if (winningPlayers != null) {
            winningPlayers = winningPlayers.stream()
                    .filter(p -> playerIds.contains(p.getId()))
                    .toList();
        }

        this.participants = new ArrayList<>(participants);
    }

    /**
     * Returns the script (that is, the list of characters that may appear in this game) that was used in this game.
     *
     * @return the script used in this game
     */
    public Script getScript() {
        return script;
    }

    /**
     * Sets the script (that is, the list of characters that may appear in this game) that was used in this game.
     *
     * @param script the script used in this game. May not be null.
     */
    public void setScript(Script script) {
        this.script = Objects.requireNonNull(script);
    }

    /**
     * Returns the alignment that won this game. If null, use {@link #getWinningPlayers()} to determine the winning
     * players (there may be instances where not a single alignment wins, but the situation is more complex or a draw).
     *
     * @return the alignment that won this game
     */
    public Alignment getWinningAlignment() {
        return winningAlignment;
    }

    /**
     * Sets the alignment that won this game. If the winning team is not defined by their shared alignment but by a more
     * complex scenario, use {@link #setWinningPlayers(List)} to set the winning players manually.
     *
     * @param winningAlignment the alignment that won the game. May not be null.
     */
    public void setWinningAlignment(Alignment winningAlignment) {
        this.winningAlignment = Objects.requireNonNull(winningAlignment);
        this.winningPlayers = participants.stream()
                .map(PlayerParticipation::getPlayer)
                .filter(Objects::nonNull)
                .filter(p -> {
                    PlayerParticipation participation = participants.stream()
                            .filter(pp -> pp.getPlayer() != null && pp.getPlayer().getId().equals(p.getId()))
                            .findFirst()
                            .orElseThrow();
                    return participation.getEndAlignment() == winningAlignment;
                })
                .toList();
    }

    /**
     * Returns a free-form optional description of this game, which may include some notes about how the game went,
     * strategies of players, etc.
     *
     * @return a free-form optional description of this game
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the free-form optional description of this game, which may include some notes about how the game went,
     * strategies of players, etc.
     *
     * @param description a free-form optional description of this game
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the players that won this game. By default, these are the players whose alignment matches the winning
     * alignment. In more complex scenarios, this method may be overridden.
     *
     * @return the list of players that won this game
     */
    public List<Player> getWinningPlayers() {
        if (winningAlignment != null) {
            return getParticipants().stream()
                    .filter(entry -> entry.getEndAlignment() == getWinningAlignment())
                    .map(PlayerParticipation::getPlayer)
                    .filter(Objects::nonNull)
                    .toList();
        }
        return winningPlayers;
    }

    /**
     * Manually sets the list of players that won this game. Only use this if the winning players are not defined by
     * their alignment (use {@link #setWinningAlignment(Alignment)} if one of the two alignments won). Calling this
     * method with a valid list of players will set the winning alignment to null.
     *
     * @param winningPlayers the list of players that won the game. May not be null, contain the same player
     *         multiple times.
     */
    public void setWinningPlayers(List<Player> winningPlayers) {
        if (containsDuplicates(winningPlayers.stream().map(Player::getId).toList())) {
            throw new IllegalArgumentException("The same player cannot win multiple times in the same game.");
        }
        if (winningPlayers.contains(null)) {
            throw new IllegalArgumentException("Winning players may not contain null values.");
        }
        // we allow players to win that did not participate in the game (e.g. storyteller wins or similar)
        this.winningPlayers = winningPlayers;
        this.winningAlignment = null;
    }

    /**
     * Returns the human-readable name of this game.
     *
     * @return the human-readable name of this game
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the human-readable name of this game.
     *
     * @param name the human-readable name of this game. May not be null.
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Game name may not be null");
    }

    private <T> boolean containsDuplicates(Collection<T> collection) {
        return new HashSet<>(collection).size() != collection.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(version, game.version)
                && Objects.equals(participants, game.participants) && Objects.equals(script,
                game.script) && winningAlignment == game.winningAlignment && Objects.equals(description,
                game.description) && Objects.equals(winningPlayers, game.winningPlayers)
                && Objects.equals(name, game.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, participants, script, winningAlignment, description, winningPlayers, name);
    }
}
