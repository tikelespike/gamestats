package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Database representation of a game of Blood on the Clocktower.
 */
@Entity(name = "games")
public class GameEntity extends AbstractEntity {

    private static final int DESCRIPTION_MAX_LENGTH = 5000;
    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ScriptEntity script;

    @Enumerated(EnumType.STRING)
    private AlignmentEntity winningAlignment;

    @Column(length = DESCRIPTION_MAX_LENGTH)
    private String description;

    @NotNull
    private String name;

    @NotNull
    @OneToMany(
            mappedBy = "game",
            fetch = EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderColumn(name = "participant_order")
    private List<PlayerParticipationEntity> participants;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "game_winning_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "player_id",
                    foreignKey = @ForeignKey(
                            foreignKeyDefinition = "FOREIGN KEY (player_id) REFERENCES players(id) "
                                    + "ON DELETE CASCADE"
                    )
            )
    )
    private List<PlayerEntity> winningPlayers;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "game_storytellers",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "player_id",
                    foreignKey = @ForeignKey(
                            foreignKeyDefinition = "FOREIGN KEY (player_id) REFERENCES players(id) "
                                    + "ON DELETE CASCADE"
                    )
            )
    )
    private List<PlayerEntity> storytellers;

    /**
     * Creates a new game entity with uninitialized fields. This constructor is used by the JPA provider to create a new
     * instance of this entity from the database.
     */
    protected GameEntity() {
    }

    /**
     * Creates a new game entity.
     *
     * @param id unique identifier of this game
     * @param version version counter for optimistic locking
     * @param script the script used in this game
     * @param winningAlignment the alignment that won the game
     * @param description a free-form optional description of this game
     * @param participants the list of player participations in this game
     * @param winningPlayers the list of players that won the game, if not defined by their alignment
     * @param name human-readable name of this game (may not be null)
     * @param storytellers the list of players that acted as storytellers for this game
     */
    public GameEntity(Long id, Long version, ScriptEntity script, AlignmentEntity winningAlignment, String description,
                      List<PlayerParticipationEntity> participants, List<PlayerEntity> winningPlayers, String name,
                      List<PlayerEntity> storytellers) {
        super(id, version);
        this.script = script;
        this.winningAlignment = winningAlignment;
        this.description = description;
        setParticipants(participants);
        this.winningPlayers = winningPlayers;
        this.name = name;
        this.storytellers = storytellers;
    }

    /**
     * Returns the script used in this game.
     *
     * @return the script used in this game
     */
    public ScriptEntity getScript() {
        return script;
    }

    /**
     * Sets the script used in this game. This method is used by the JPA provider to set the script when loading this
     * game from the database.
     *
     * @param script the script used in this game
     */
    protected void setScript(ScriptEntity script) {
        this.script = script;
    }

    /**
     * Returns the alignment that won this game.
     *
     * @return the alignment that won this game
     */
    public AlignmentEntity getWinningAlignment() {
        return winningAlignment;
    }

    /**
     * Sets the alignment that won this game. This method is used by the JPA provider to set the winning alignment when
     * loading this game from the database.
     *
     * @param winningAlignment the alignment that won this game
     */
    protected void setWinningAlignment(AlignmentEntity winningAlignment) {
        this.winningAlignment = winningAlignment;
    }

    /**
     * Returns a free-form optional description of this game.
     *
     * @return a free-form optional description of this game
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a free-form optional description of this game. This method is used by the JPA provider to set the
     * description when loading this game from the database.
     *
     * @param description a free-form optional description of this game
     */
    protected void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the list of player participations in this game.
     *
     * @return the list of player participations in this game
     */
    public List<PlayerParticipationEntity> getParticipants() {
        return new ArrayList<>(participants);
    }

    /**
     * Sets the list of player participations in this game. This method is used by the JPA provider to set the
     * participants when loading this game from the database.
     *
     * @param participants the list of player participations in this game
     */
    protected void setParticipants(List<PlayerParticipationEntity> participants) {
        this.participants = new ArrayList<>(participants);
        this.participants.forEach(p -> p.setGame(this));
    }

    /**
     * Returns the list of winning players.
     *
     * @return the list of winning players
     */
    public List<PlayerEntity> getWinningPlayers() {
        return winningPlayers;
    }

    /**
     * Sets the list of winning players.
     *
     * @param winningPlayers the list of winning players
     */
    public void setWinningPlayers(List<PlayerEntity> winningPlayers) {
        this.winningPlayers = winningPlayers;
    }

    /**
     * Returns the list of players that acted as storytellers for this game.
     *
     * @return the list of storytellers for this game
     */
    public List<PlayerEntity> getStorytellers() {
        return storytellers;
    }

    /**
     * Sets the list of players that acted as storytellers for this game.
     *
     * @param storytellers the list of storytellers for this game
     */
    protected void setStorytellers(List<PlayerEntity> storytellers) {
        this.storytellers = storytellers;
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
     * Sets the human-readable name of this game. This method is used by the JPA provider to set the name when loading
     * this game from the database.
     *
     * @param name the human-readable name of this game (may not be null)
     */
    protected void setName(String name) {
        this.name = name;
    }
}
