package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.Validateable;
import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for a player's participation in a game.
 *
 * @param playerId id of the player
 * @param initialCharacterId id of the character the player started with
 * @param initialAlignment the alignment the player started with
 * @param endCharacterId id of the character the player ended with
 * @param endAlignment the alignment the player ended with (may differ from startAlignment due to character
 *         abilities)
 * @param isAliveAtEnd whether the player was still alive at the end of the game
 */
@Schema(
        name = "Player Participation",
        description = "Information about a player's participation in a game, including their character and alignment."
)
public record PlayerParticipationDTO(
        @Schema(
                description = "Unique numerical identifier of the player whose participation in a game is represented"
                        + " by this object. Is required and must be an existing player id.",
                example = "42"
        ) Long playerId,
        @Schema(
                description = "Unique numerical identifier of the character the player started the game with. Is "
                        + "required and must be an existing character id.",
                example = "5"
        ) Long initialCharacterId,
        @Schema(
                description = "The alignment the player started with. Defaults to the default alignment of the initial "
                        + "character if not provided.",
                example = "good"
        ) AlignmentDTO initialAlignment,
        @Schema(
                description = "Unique numerical identifier of the character the player ended the game with. Most of "
                        + "the time, this will be equal to the initial character, but it may differ if the player "
                        + "changes character during the game. Defaults to the initial character if not provided.",
                example = "5"
        ) Long endCharacterId,
        @Schema(
                description = "The alignment the player ended with. Most of the time, this will be equal to the "
                        + "initial alignment, but it may differ if the player changes alignment during the game. "
                        + "Defaults to the default alignment of the end character if not provided.",
                example = "evil"
        ) AlignmentDTO endAlignment,
        @Schema(
                description = "Whether the player was (still) alive when the game ended. Defaults to false.",
                example = "true"
        ) boolean isAliveAtEnd
) implements Validateable {
    @Override
    public ValidationResult validate() {
        return new ValidationChain(
                new RequiredFieldCheck("playerId", playerId),
                new RequiredFieldCheck("initialCharacterId", initialCharacterId)
        ).validate();
    }
}
