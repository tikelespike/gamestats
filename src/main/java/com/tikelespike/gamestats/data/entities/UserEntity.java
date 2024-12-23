package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * The database representation of an application user.
 */
@Table()
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(targetClass = UserRoleEntity.class, fetch = FetchType.EAGER)
    private Set<UserRoleEntity> roles;

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
     * @param email email address used for login
     * @param password password used for login
     * @param roles the roles assigned to the user (for permission management)
     */
    public UserEntity(Long id, String email, String password, Set<UserRoleEntity> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
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
     * Sets the unique identifier of the user. This method is used by the JPA provider to set the identifier when the
     * entity is read from the database.
     *
     * @param id the new unique identifier of the user
     */
    protected void setId(Long id) {
        this.id = id;
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
     * Returns the roles assigned to the user. The roles are used for permission management.
     *
     * @return a copy of the set of roles assigned to the user
     */
    public Set<UserRoleEntity> getRolesCopy() {
        return new HashSet<>(roles);
    }

    /**
     * Returns the roles assigned to the user. The roles are used for permission management. This method is used by the
     * JPA provider to set the roles when the entity is read from the database. To get a copy of the roles, use
     * {@link #getRolesCopy()}.
     *
     * @return the roles assigned to the user
     */
    protected Set<UserRoleEntity> getRoles() {
        return roles;
    }

    /**
     * Sets the roles assigned to the user.
     *
     * @param roles the roles assigned to the user
     */
    public void setRoles(Set<UserRoleEntity> roles) {
        this.roles = roles;
    }
}
