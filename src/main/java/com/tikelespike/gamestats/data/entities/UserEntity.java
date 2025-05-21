package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * The database representation of an application user.
 */
@Table()
@Entity(name = "users")
public class UserEntity extends AbstractEntity {

    private String name;
    private String email;
    private String password;

    @OneToOne(
            fetch = FetchType.EAGER,
            mappedBy = "owner"
    )
    private PlayerEntity player;

    @Enumerated(EnumType.STRING)
    private UserRoleEntity role;

    /**
     * Creates a new user entity with uninitialized fields. This constructor is used by the JPA provider to create a new
     * instance of this entity from the database.
     */
    protected UserEntity() {
    }

    /**
     * Creates a new user entity.
     *
     * @param id unique identifier of the user
     * @param version version counter for optimistic locking
     * @param name human-readable name of the user
     * @param email email address used for login
     * @param password password used for login
     * @param player the game participant associated with the user (if the user participates in games)
     * @param role the role assigned to the user (for permission management)
     */
    public UserEntity(Long id, Long version, String name, String email, String password,
                      PlayerEntity player, UserRoleEntity role) {
        super(id, version);
        this.name = name;
        this.email = email;
        this.password = password;
        this.player = player;
        this.role = role;
    }

    /**
     * Returns the human-readable name of the user.
     *
     * @return the human-readable name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the human-readable name of the user.
     *
     * @param name the new human-readable name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password used for login.
     *
     * @return password used for login
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password used for login.
     *
     * @param password password used for login
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the player object associated with the user (if the user participates in games). The player object is what
     * tracks a players game statistics. Since it is possible for players to participate in games without having an
     * application account, these are separate concepts, even though a real-world person usually has both.
     *
     * @return the game participant associated with the user (if the user participates in games)
     */
    public PlayerEntity getPlayer() {
        return player;
    }

    /**
     * Sets the player object associated with the user (if the user participates in games). The player object is what
     * tracks a players game statistics. Since it is possible for players to participate in games without having an
     * application account, these are separate concepts, even though a real-world person usually has both.
     *
     * @param player the game participant associated with the user (if the user participates in games)
     */
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    /**
     * Returns the role assigned to the user. The role is used for permission management.
     *
     * @return the role assigned to the user
     */
    public UserRoleEntity getRole() {
        return role;
    }

    /**
     * Sets the role assigned to the user.
     *
     * @param role the role assigned to the user
     */
    public void setRole(UserRoleEntity role) {
        this.role = role;
    }
}
