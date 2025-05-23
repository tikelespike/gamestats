package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The database representation of a game participant.
 */
@Table()
@Entity(name = "players")
public class PlayerEntity extends AbstractEntity {

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "owner_id",
            referencedColumnName = "id"
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private UserEntity owner;

    /**
     * Creates a new player entity with uninitialized fields. This constructor is used by the JPA provider to create a
     * new instance of this entity from the database.
     */
    protected PlayerEntity() {
    }

    /**
     * Creates a new player entity.
     *
     * @param id unique identifier of the player
     * @param version version counter for optimistic locking
     * @param name human-readable name of the player (usually the real-world name of the person)
     * @param owner the user that owns this player, or null if the player is not owned by any user
     */
    public PlayerEntity(Long id, Long version, String name, UserEntity owner) {
        super(id, version);
        this.name = name;
        this.owner = owner;
    }

    /**
     * Returns the human-readable name of the player.
     *
     * @return the human-readable name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the human-readable name of the player.
     *
     * @param name the new human-readable name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user that owns this player.
     *
     * @return the user that owns this player, or null if the player is not owned by any user
     */
    public UserEntity getOwner() {
        return owner;
    }

    /**
     * Sets the user that owns this player.
     *
     * @param owner the new owner of this player.
     */
    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }
}
