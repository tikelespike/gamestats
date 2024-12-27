package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table()
@Entity(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
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
     * @param name human-readable name of the player (usually the real-world name of the person)
     * @param owner the user that owns this player, or null if the player is not owned by any user
     */
    public PlayerEntity(Long id, String name, UserEntity owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    /**
     * Returns the unique identifier of the player.
     *
     * @return the unique identifier of the player
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the player. This method is used by the JPA provider to set the identifier when
     * creating a new instance of this entity from the database.
     *
     * @param id the unique identifier of the player
     */
    protected void setId(Long id) {
        this.id = id;
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
