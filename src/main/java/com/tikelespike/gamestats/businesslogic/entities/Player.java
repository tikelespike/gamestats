package com.tikelespike.gamestats.businesslogic.entities;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * A player that participates in games. This is different from an application user - there may be players that are
 * recorded in the game data but do not have an account to log into the application on their own. Similarly, there may
 * be users that have accounts but do not participate in any games. A player can be owned by a user, which means that
 * the user has control over the player and can manage it. This usually means that the user and the player both
 * represent the same real-world person.
 */
public class Player implements HasId {
    private final Long id;
    private String name;
    private User owner;

    /**
     * Creates a new player. This is a full constructor that is useful for creating a player from existing data. To
     * create a conceptually new player, use the other constructors.
     *
     * @param id unique identifier of the player
     * @param name human-readable name of the player (usually the real-world name of the person)
     * @param owner the user that owns this player, or null if the player is not owned by any user
     */
    public Player(Long id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    /**
     * Creates a new player with unassigned id number. This constructor is used when creating a new player, as the id is
     * assigned by the database.
     *
     * @param owner the user that owns this player. May not be null (use overloaded constructor to create
     *         ownerless players).
     */
    public Player(@NotNull User owner) {
        this(null, null, Objects.requireNonNull(owner));
    }

    /**
     * Creates an unowned player with unassigned id number. This constructor is used when creating a new player, as the
     * id is assigned by the database. The player can be assigned an owner later.
     *
     * @param name human-readable name of the player (usually the real-world name of the person). Once the
     *         player is assigned an owner, the owners name will override this. May not be null or empty.
     */
    public Player(@NotNull String name) {
        this(null, name, null);
        if (name.isBlank()) {
            throw new IllegalArgumentException("Player name must not be empty");
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * Returns the human-readable name of the player.
     *
     * @return the human-readable name of the player
     */
    public String getName() {
        return owner == null ? name : owner.getName();
    }

    /**
     * Sets the human-readable name of the player. If the player has an owner, the owner's name will override this, and
     * this method will have no effect.
     *
     * @param name the new human-readable name of the player. May not be null or empty.
     */
    public void setName(@NotNull String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Player name must not be empty");
        }
        this.name = name;
    }

    /**
     * Returns the user that owns this player. If the player is not owned by any user, this method will return null.
     *
     * @return the user that owns this player, or null if the player is not owned by any user
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the user that owns this player.
     *
     * @param owner the user that owns this player. May not be null.
     */
    public void setOwner(@NotNull User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;


        boolean equalOwners;
        if (owner == null || player.owner == null) {
            equalOwners = owner == player.owner;
        } else {
            equalOwners = Objects.equals(owner.getId(), player.owner.getId());
        }
        return Objects.equals(id, player.id) && Objects.equals(name, player.name) && equalOwners;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner.getId());
    }
}
