package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the player in its full state.
 *
 * @param id unique numerical identifier
 * @param version version counter for optimistic locking
 * @param name human-readable name
 * @param ownerId unique numerical identifier of the owning user
 *
 * @see PlayerCreationDTO
 */
@Schema(
        name = "Player",
        description = "A participant in games. Can be linked to a user account, if that player is registered in the "
                + "application."
)
public record PlayerDTO(
        @Schema(
                description = "Unique numerical identifier of the player.",
                example = "12"
        ) Long id,
        @Schema(
                description = "Version counter for optimistic locking.",
                example = "1"
        ) Long version,
        @Schema(
                description = "Player's human readable name.",
                example = "Max Mustermann"
        ) String name,
        @Schema(
                description = "Unique numerical identifier of the owning user.",
                example = "42"
        ) Long ownerId
) {

}
