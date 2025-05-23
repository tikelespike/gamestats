package com.tikelespike.gamestats.businesslogic.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * An application user. This is not necessarily a player of the game, but a user of the game statistics application, and
 * is as such used for authentication and authorization within the app.
 */
public class User implements UserDetails, HasId, HasVersion {

    private final Long id;
    private final Long version;
    private UserRole role;
    private String name;
    private String email;
    private String password;
    private transient Player player;

    /**
     * Creates a new user.
     *
     * @param id unique identifier of the user. May not be null.
     * @param version version counter for optimistic locking. May not be null.
     * @param name full name of the user. May not be null or blank.
     * @param email email address used for login. May not be null or blank.
     * @param password password used for login. May not be null or blank.
     * @param player the player associated with this user (encapsulates the data of the human participating in
     *         the game)
     * @param role the role assigned to the user (for permission management)
     */
    public User(Long id, Long version, String name, String email, String password, Player player, UserRole role) {
        this.id = Objects.requireNonNull(id);
        this.version = Objects.requireNonNull(version);
        this.name = Objects.requireNonNull(name);
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name may not be blank");
        }
        this.email = Objects.requireNonNull(email);
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email may not be blank");
        }
        this.password = Objects.requireNonNull(password);
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password may not be blank");
        }
        this.player = player;
        this.role = role != null ? role : UserRole.defaultRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
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
     * Sets the new password of the user.
     *
     * @param password the new password of the user
     */
    public void setPassword(String password) {
        this.password = password;
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
     * Returns the player associated with this user. The player encapsulates the data of the human participating in the
     * game. This is optional, as not all people with user accounts in this app play the game themselves (similarly, not
     * all players may have an account in the app).
     *
     * @return the player associated with this user
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player associated with this user. The player encapsulates the data of the human participating in the
     * game. This is optional, as not all people with user accounts in this app play the game themselves (similarly, not
     * all players may have an account in the app).
     *
     * @param player the player associated with this user
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Returns the role assigned to the user. The role is used for permission management.
     *
     * @return the role assigned to the user
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Assigns a role to the user. The role is used for permission management. A user can only have one role at a time.
     *
     * @param role the role assigned to the user
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        boolean equalPlayers;
        if (player == null || user.player == null) {
            equalPlayers = player == user.player;
        } else {
            equalPlayers = Objects.equals(player.getId(), user.player.getId());
        }
        return Objects.equals(id, user.id) && Objects.equals(role, user.role)
                && Objects.equals(name, user.name) && Objects.equals(email, user.email)
                && Objects.equals(password, user.password) && equalPlayers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, name, email, password, player.getId());
    }
}
