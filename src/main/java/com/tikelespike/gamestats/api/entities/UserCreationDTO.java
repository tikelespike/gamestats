package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.Validateable;
import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * REST transfer object for the user sign-up request.
 *
 * @param name user's human readable name (preferably real-world name)
 * @param email user's email address
 * @param password user's password
 * @param permissionLevel permission level of the user
 * @param playerId unique numerical identifier of the player to associate with this user (optional)
 */
@Schema(
        name = "User (creation)",
        description = "Request to create a user. A user is an account that can log into the application and access "
                + "its features based on its permissions. Optionally, a player can be associated with the user."
)
public record UserCreationDTO(
        @Schema(
                description = "User's human-readable name (preferably real-world name)",
                example = "Max Mustermann"
        ) String name,
        @Schema(
                description = "User's email address",
                example = "user@example.com"
        ) String email,
        @Schema(
                description = "User's password",
                example = "password123"
        ) String password,
        @Schema(
                description = "Permission level of the user. Default is user. A user can only change their own user "
                        + "information and credentials. A storyteller can create and manage games and game content "
                        + "like characters and scripts. An administrator can manage other users and their roles.",
                example = "user"
        ) UserRoleDTO permissionLevel,
        @Schema(
                description = "Unique numerical identifier of the player to associate with this user. If not provided, "
                        + "no player will be associated with the user.",
                example = "42"
        ) Long playerId
) implements Validateable {
    @Override
    public ValidationResult validate() {
        return new ValidationChain(
                new RequiredFieldCheck("name", name),
                new RequiredFieldCheck("email", email),
                new RequiredFieldCheck("password", password),
                new RequiredFieldCheck("permissionLevel", permissionLevel)
        ).validate();
    }
}
