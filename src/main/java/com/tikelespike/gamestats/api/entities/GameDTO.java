package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.EitherFieldRequiredCheck;
import com.tikelespike.gamestats.api.validation.checks.MatchingIdCheck;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * REST transfer object for a game.
 *
 * @param id unique numerical identifier
 * @param version version counter for optimistic locking
 * @param name human-readable name of this game
 * @param description free-form description of the game (optional)
 * @param scriptId id of the script used in this game
 * @param winningAlignment the alignment that won the game (optional, use winningPlayerIds if not applicable)
 * @param winningPlayerIds list of player ids that won the game (optional, use winningAlignment if not
 *         applicable)
 * @param participants list of player participations in this game
 */
@Schema(
        name = "Game",
        description = "A single game of Blood on the Clocktower played with a set of concrete players. Contains "
                + "statistical data about each player's participation in the game, like the character they played and"
                + " whether they won the game."
)
public record GameDTO(
        @Schema(
                description = "Unique numerical identifier of the game.",
                example = "42"
        ) Long id,
        @Schema(
                description = "Version counter for optimistic locking.",
                example = "1"
        ) Long version,
        @Schema(
                description = "Human-readable name of this game.",
                example = "Tom's birthday first game"
        ) String name,
        @Schema(
                description = "Free-form description of the game.",
                example = "A very intense game with lots of bluffing! The good team won due to the great play of the "
                        + "Fortune Teller."
        ) String description,
        @Schema(
                description = "Unique numerical identifier of the script (collection of characters) used in this game.",
                example = "5"
        ) Long scriptId,
        @Schema(
                description = "The alignment that won the game. Either this or winningPlayerIds must be set.",
                example = "good"
        ) AlignmentDTO winningAlignment,
        @Schema(
                description = "List of player ids that won the game. Either this or winningAlignment must be set. If "
                        + "winningAlignment is set, winningPlayerIds will be ignored.",
                example = "[1, 2, 3]"
        ) Long[] winningPlayerIds,
        @Schema(
                description = "List of players and their game-specific data for this game."
        ) PlayerParticipationDTO[] participants
) {
    /**
     * Validates this DTO in the context of updating a game.
     *
     * @param pathId the id from the path parameter
     *
     * @return result of the validation
     */
    public ValidationResult validateUpdate(Long pathId) {
        return new ValidationChain(
                new RequiredFieldCheck("id", id),
                new MatchingIdCheck(pathId, id),
                new RequiredFieldCheck("version", version),
                new RequiredFieldCheck("scriptId", scriptId),
                new RequiredFieldCheck("participants", participants),
                new RequiredFieldCheck("name", name),
                this::checkAllParticipationsValid,
                this::checkNoDuplicatePlayersInParticipations,
                new EitherFieldRequiredCheck(
                        new EitherFieldRequiredCheck.Field("winningAlignment", winningAlignment),
                        new EitherFieldRequiredCheck.Field("winningPlayerIds", winningPlayerIds)
                ),
                this::checkWinningPlayerIdsValid
        ).validate();
    }

    private ValidationResult checkWinningPlayerIdsValid() {
        if (winningAlignment != null) {
            return ValidationResult.valid();
        }
        if (Arrays.stream(winningPlayerIds)
                .anyMatch(Objects::isNull)) {
            return ValidationResult.invalid("Winning player ids must not contain null values.");
        }
        Collection<Long> participatingPlayers = Arrays.stream(participants)
                .map(PlayerParticipationDTO::playerId)
                .toList();
        List<Long> invalidIds =
                Arrays.stream(winningPlayerIds).filter(playerId -> !participatingPlayers.contains(playerId))
                        .toList();
        if (!invalidIds.isEmpty()) {
            return ValidationResult.invalid("Winning player ids " + invalidIds
                    + " are not participating in the game.");
        }
        return ValidationResult.valid();
    }

    private ValidationResult checkNoDuplicatePlayersInParticipations() {
        if (containsDuplicates(Arrays.stream(participants).map(PlayerParticipationDTO::playerId).toArray())) {
            return ValidationResult.invalid("Duplicate player ids found in participants. Each player can "
                    + "only participate once in a game.");
        }
        return ValidationResult.valid();
    }

    private ValidationResult checkAllParticipationsValid() {
        if (Arrays.stream(participants)
                .anyMatch(Objects::isNull)) {
            return ValidationResult.invalid("Participants must not contain null values.");
        }

        return Arrays.stream(participants)
                .map(PlayerParticipationDTO::validate)
                .filter(r -> !r.isValid())
                .findFirst()
                .orElse(ValidationResult.valid());
    }

    private boolean containsDuplicates(Object[] array) {
        return array.length != Arrays.stream(array).distinct().count();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameDTO gameDTO = (GameDTO) o;
        return Objects.equals(id, gameDTO.id) && Objects.equals(version, gameDTO.version)
                && Objects.equals(scriptId, gameDTO.scriptId) && Objects.equals(description,
                gameDTO.description) && Objects.deepEquals(winningPlayerIds, gameDTO.winningPlayerIds)
                && winningAlignment == gameDTO.winningAlignment && Objects.deepEquals(participants,
                gameDTO.participants) && Objects.equals(name, gameDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, scriptId, description, winningAlignment, Arrays.hashCode(winningPlayerIds),
                Arrays.hashCode(participants), name);
    }

    @Override
    public String toString() {
        return "GameDTO{"
                + "id=" + id
                + ", version=" + version
                + ", scriptId=" + scriptId
                + ", description='" + description + '\''
                + ", winningAlignment=" + winningAlignment
                + ", winningPlayerIds=" + Arrays.toString(winningPlayerIds)
                + ", participants=" + Arrays.toString(participants)
                + ", name='" + name + '\''
                + '}';
    }
}
