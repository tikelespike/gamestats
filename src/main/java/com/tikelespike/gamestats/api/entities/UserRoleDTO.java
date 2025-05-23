package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the permission level of a user.
 */
@Schema(
        name = "User Role",
        description = "Permission level of a user."
)
public enum UserRoleDTO {
    // note: enum values here violate the ALL_CAPS naming convention on purpose, since they get directly mapped to
    // strings in JSON, which should be lower case.

    /**
     * A regular user with no special permissions. This role should be granted to every registered user.
     */
    @Schema(
            name = "User",
            description = "A regular user with no special permissions. This role should be granted to every "
                    + "registered user."
    )
    user,

    /**
     * A user that can create and manage games/game content.
     */
    @Schema(
            name = "Storyteller",
            description = "A user that can create and manage game content."
    )
    storyteller,

    /**
     * A user that can manage other users and their roles.
     */
    @Schema(
            name = "Administrator",
            description = "A user that has all permissions of a storyteller and can manage other users and their roles."
    )
    admin
}
