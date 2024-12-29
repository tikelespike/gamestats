package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the player creation request.
 *
 * @param name player name
 * @param ownerId player owner id
 */
@Schema(
        name = "Player (creation)",
        description = "A participant in games, as required by the POST endpoint for creating players."
)
public record PlayerCreationDTO(
        @Schema(
                description = "Player's human readable name. Can be omitted if ownerId is set, but is required "
                        + "otherwise. Will have no effect if ownerId is set, since the owner's"
                        + " name will be used instead.",
                example = "Max Mustermann"
        ) String name,
        @Schema(
                description = "Unique numerical identifier of the owning user. Can be omitted if the player does not "
                        + "have a user account, but then, the player name has to be set.",
                example = "42"
        ) Long ownerId
) {
}
