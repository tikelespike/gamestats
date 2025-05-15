package com.tikelespike.gamestats.businesslogic.entities;

/**
 * Request to create a user, with data about the new user.
 *
 * @param name user display name (preferably real name)
 * @param email user email address
 * @param password user chosen password
 * @param role user permission level
 */
public record UserCreationRequest(String name, String email, String password, UserRole role) {

}
