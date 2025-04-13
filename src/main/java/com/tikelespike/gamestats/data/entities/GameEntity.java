package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Database representation of a game of Blood on the Clocktower.
 */
@Entity(name = "games")
public class GameEntity extends AbstractEntity {
    @NotNull
    @ManyToOne(fetch = EAGER)
    private ScriptEntity script;

    @Enumerated(EnumType.STRING)
    private AlignmentEntity winningAlignment;

    private String description;

    @NotNull
    @OneToMany(
            mappedBy = "game",
            fetch = EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PlayerParticipationEntity> participants;

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
     */
    public GameEntity(Long id, Long version, ScriptEntity script, AlignmentEntity winningAlignment, String description,
                      List<PlayerParticipationEntity> participants) {
        super(id, version);
        this.script = script;
        this.winningAlignment = winningAlignment;
        this.description = description;
        this.participants = new ArrayList<>(participants);
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
    }
}
