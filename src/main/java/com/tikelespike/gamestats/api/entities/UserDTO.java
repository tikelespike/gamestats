package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object of a user (when retrieved).
 *
 * @param id unique numerical identifier
 * @param version version counter for optimistic locking
 * @param name user's human-readable name (preferably real-world name)
 * @param email user's email address
 * @param permissionLevel permission level of the user
 * @param playerId unique numerical identifier of the player associated with this application user
 */
@Schema(
        name = "User",
        description = "A user is an account that can log into the application and access its features based on its "
                + "permissions. A user is usually associated with a player. While the player represents the person "
                + "participating in the actual game, the user is the account used to log into the application."
)
public record UserDTO(
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
                description = "Permission level of the user. Default is user. A user can only change their own user "
                        + "information and credentials. A storyteller can create and manage games and game content "
                        + "like characters and scripts. An administrator can manage other users and their roles.",
                example = "user"
        ) UserRoleDTO permissionLevel,
        @Schema(
                description = "Unique numerical identifier of the player associated with this application user. The "
                        + "player is the person playing the game, while the user is the account used to log into the "
                        + "application.",
                example = "42"
        ) Long playerId
) {
}
