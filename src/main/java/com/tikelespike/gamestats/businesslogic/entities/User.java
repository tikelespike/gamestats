package com.tikelespike.gamestats.businesslogic.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * An application user. This is not necessarily a player of the game, but a user of the game statistics application, and
 * is as such used for authentication and authorization within the app.
 */
public class User implements UserDetails {

    private final Long id;
    private final Set<UserRole> roles;
    private String name;
    private String email;
    private String password;
    private Player player;

    /**
     * Creates a new user object with unassigned id number. This constructor is used when creating a new user, as the id
     * is assigned by the database. To register a new user in the application, use the
     * {@link com.tikelespike.gamestats.businesslogic.UserService#signUp(SignupRequest)} service.
     *
     * @param email email address used for login
     * @param password password used for login
     * @param roles the roles assigned to the user (for permission management)
     */
    public User(String name, String email, String password, Set<UserRole> roles) {
        this(null, name, email, password, null, roles);
    }

    /**
     * Creates a new user. This constructor is used when loading an existing user from the database. To create a new
     * user without specifying an id, use {@link #User(String, String, String, Set)}.
     *
     * @param id unique identifier of the user
     * @param email email address used for login
     * @param password password used for login
     * @param roles the roles assigned to the user (for permission management)
     */
    public User(Long id, String name, String email, String password, Player player, Set<UserRole> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.player = player;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.toString())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
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
     * Returns the unique identifier of the user.
     *
     * @return the unique identifier of the user
     */
    public Long getId() {
        return id;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Returns the roles assigned to the user. The roles are used for permission management.
     *
     * @return a copy of the set of roles assigned to the user
     */
    public Set<UserRole> getRoles() {
        return new HashSet<>(roles);
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
        return Objects.equals(id, user.id) && Objects.equals(roles, user.roles)
                && Objects.equals(name, user.name) && Objects.equals(email, user.email)
                && Objects.equals(password, user.password) && equalPlayers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roles, name, email, password, player.getId());
    }
}
