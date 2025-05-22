package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.MatchingIdCheck;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for updating a user.
 *
 * @param id unique numerical identifier
 * @param version version counter for optimistic locking
 * @param name user's human-readable name (preferably real-world name)
 * @param email user's email address
 * @param password new password (optional, only included if password should be changed)
 * @param permissionLevel permission level of the user
 * @param playerId unique numerical identifier of the player to associate with this user (optional)
 */
@Schema(
        name = "User Update",
        description = "Data for updating an existing user. All fields except password and playerId are required. "
                + "Password is only included if it should be changed. PlayerId is optional and can be used to "
                + "associate a player with the user."
)
public record UserUpdateDTO(
        @Schema(
                description = "Unique numerical identifier of the user.",
                example = "42"
        ) Long id,
        @Schema(
                description = "Version counter for optimistic locking.",
                example = "1"
        ) Long version,
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
) {
    /**
     * Validates this DTO in the context of updating a user.
     *
     * @return a validation result indicating whether the DTO is valid or not
     */
    public ValidationResult validateUpdate(Long pathId) {
        return new ValidationChain(
                new RequiredFieldCheck("id", id),
                new MatchingIdCheck(pathId, id),
                new RequiredFieldCheck("version", version),
                new RequiredFieldCheck("name", name),
                new RequiredFieldCheck("email", email),
                new RequiredFieldCheck("permissionLevel", permissionLevel)
        ).validate();
    }
}
