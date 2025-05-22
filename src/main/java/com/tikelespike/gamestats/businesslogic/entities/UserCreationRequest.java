package com.tikelespike.gamestats.businesslogic.entities;

/**
 * Request to create a user, with data about the new user.
 *
 * @param name user display name (preferably real name)
 * @param email user email address
 * @param password user chosen password
 * @param role user permission level
 * @param player player to associate with this user (optional)
 */
public record UserCreationRequest(
        String name,
        String email,
        String password,
        UserRole role,
        Player player
) {
    /**
     * Creates a new user creation request.
     *
     * @param name display name of the user (ideally real name)
     * @param email email address of the user
     * @param password password chosen by the user (in plain text)
     * @param role permission level of the user (default is user)
     * @param player player to associate with this user (optional)
     */
    public UserCreationRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
