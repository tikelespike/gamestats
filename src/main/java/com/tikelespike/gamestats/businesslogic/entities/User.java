package com.tikelespike.gamestats.businesslogic.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * An application user. This is not necessarily a player of the game, but a user of the game statistics application, and
 * is as such used for authentication and authorization within the app.
 */
public class User implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Set<UserRole> roles;

    /**
     * Creates a new user object with unassigned id number. This constructor is used when creating a new user, as the id
     * is assigned by the database. To register a new user in the application, use the
     * {@link com.tikelespike.gamestats.businesslogic.UserService#signUp(SignupRequest)} service.
     *
     * @param email email address used for login
     * @param password password used for login
     * @param roles the roles assigned to the user (for permission management)
     */
    public User(String email, String password, Set<UserRole> roles) {
        this(null, email, password, roles);
    }

    /**
     * Creates a new user. This constructor is used when loading an existing user from the database. To create a new
     * user without specifying an id, use {@link #User(String, String, Set)}.
     *
     * @param id unique identifier of the user
     * @param email email address used for login
     * @param password password used for login
     * @param roles the roles assigned to the user (for permission management)
     */
    public User(Long id, String email, String password, Set<UserRole> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
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
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
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
     * Returns the roles assigned to the user. The roles are used for permission management.
     *
     * @return a copy of the set of roles assigned to the user
     */
    public Set<UserRole> getRoles() {
        return new HashSet<>(roles);
    }
}
