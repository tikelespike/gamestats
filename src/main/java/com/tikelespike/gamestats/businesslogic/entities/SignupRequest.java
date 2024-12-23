package com.tikelespike.gamestats.businesslogic.entities;

/**
 * A user sign-up request containing the data about the new user.
 *
 * @param email user email address
 * @param password user chosen password
 */
public record SignupRequest(String email, String password) {

}
