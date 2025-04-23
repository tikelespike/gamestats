package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Database representation of the data associated with a single player's participation in a single game.
 */
@Entity(name = "player_participations")
public class PlayerParticipationEntity extends AbstractEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private GameEntity game;

    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private PlayerEntity player;

    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    private CharacterEntity initialCharacter;

    @Enumerated(EnumType.STRING)
    private AlignmentEntity initialAlignment;

    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = true
    )
    private CharacterEntity endCharacter;

    @Enumerated(EnumType.STRING)
    private AlignmentEntity endAlignment;

    private boolean isAliveAtEnd;

    /**
     * Creates a new player participation entity with uninitialized fields. This constructor is used by the JPA provider
     * to create a new instance of this entity from the database.
     */
    protected PlayerParticipationEntity() {
    }

    /**
     * Creates a new player participation entity.
     *
     * @param id unique identifier of this participation
     * @param version version counter for optimistic locking
     * @param game the game this participation belongs to
     * @param player the player that participated (may be null)
     * @param initialCharacter the character the player started with (may be null)
     * @param initialAlignment the alignment the player started with (if null and initialCharacter is non-null,
     *         defaults to the default alignment of the initial character)
     * @param endCharacter the character the player ended with (if null and initialCharacter is non-null,
     *         defaults to the initial character)
     * @param endAlignment the alignment the player ended with (if null and endCharacter is non-null, defaults
     *         to the default alignment of the end character)
     * @param isAliveAtEnd whether the player was alive at the end of the game
     */
    public PlayerParticipationEntity(Long id, Long version, GameEntity game, PlayerEntity player,
                                     CharacterEntity initialCharacter, AlignmentEntity initialAlignment,
                                     CharacterEntity endCharacter, AlignmentEntity endAlignment,
                                     boolean isAliveAtEnd) {
        super(id, version);
        this.game = game;
        this.player = player;
        this.initialCharacter = initialCharacter;
        this.initialAlignment = initialAlignment;
        this.endCharacter = endCharacter;
        this.endAlignment = endAlignment;
        this.isAliveAtEnd = isAliveAtEnd;
    }

    /**
     * Returns the game this participation belongs to.
     *
     * @return the game this participation belongs to
     */
    public GameEntity getGame() {
        return game;
    }

    /**
     * Sets the game this participation belongs to.
     *
     * @param game the game this participation belongs to
     */
    public void setGame(GameEntity game) {
        this.game = game;
    }

    /**
     * Returns the player that participated.
     *
     * @return the player that participated
     */
    public PlayerEntity getPlayer() {
        return player;
    }

    /**
     * Sets the player that participated. This method is used by the JPA provider to set the player when loading this
     * participation from the database.
     *
     * @param player the player that participated
     */
    protected void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    /**
     * Returns the character the player started with.
     *
     * @return the character the player started with
     */
    public CharacterEntity getInitialCharacter() {
        return initialCharacter;
    }

    /**
     * Sets the character the player started with. This method is used by the JPA provider to set the initial character
     * when loading this participation from the database.
     *
     * @param initialCharacter the character the player started with
     */
    protected void setInitialCharacter(CharacterEntity initialCharacter) {
        this.initialCharacter = initialCharacter;
    }

    /**
     * Returns the alignment the player started with.
     *
     * @return the alignment the player started with
     */
    public AlignmentEntity getInitialAlignment() {
        return initialAlignment;
    }

    /**
     * Sets the alignment the player started with. This method is used by the JPA provider to set the initial alignment
     * when loading this participation from the database.
     *
     * @param initialAlignment the alignment the player started with
     */
    protected void setInitialAlignment(AlignmentEntity initialAlignment) {
        this.initialAlignment = initialAlignment;
    }

    /**
     * Returns the character the player ended with.
     *
     * @return the character the player ended with
     */
    public CharacterEntity getEndCharacter() {
        return endCharacter;
    }

    /**
     * Sets the character the player ended with. This method is used by the JPA provider to set the end character when
     * loading this participation from the database.
     *
     * @param endCharacter the character the player ended with
     */
    protected void setEndCharacter(CharacterEntity endCharacter) {
        this.endCharacter = endCharacter;
    }

    /**
     * Returns the alignment the player ended with.
     *
     * @return the alignment the player ended with
     */
    public AlignmentEntity getEndAlignment() {
        return endAlignment;
    }

    /**
     * Sets the alignment the player ended with. This method is used by the JPA provider to set the end alignment when
     * loading this participation from the database.
     *
     * @param endAlignment the alignment the player ended with
     */
    protected void setEndAlignment(AlignmentEntity endAlignment) {
        this.endAlignment = endAlignment;
    }

    /**
     * Returns whether the player was alive at the end of the game.
     *
     * @return whether the player was alive at the end of the game
     */
    public boolean isAliveAtEnd() {
        return isAliveAtEnd;
    }

    /**
     * Sets whether the player was alive at the end of the game. This method is used by the JPA provider to set the
     * alive status when loading this participation from the database.
     *
     * @param aliveAtEnd whether the player was alive at the end of the game
     */
    protected void setAliveAtEnd(boolean aliveAtEnd) {
        isAliveAtEnd = aliveAtEnd;
    }
}
