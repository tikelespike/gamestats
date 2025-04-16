package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.Validateable;
import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.EitherFieldRequiredCheck;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * REST transfer object for creating a new game.
 *
 * @param scriptId id of the script used in this game
 * @param description free-form description of the game (optional)
 * @param winningAlignment the alignment that won the game (optional, use winningPlayerIds if not applicable)
 * @param winningPlayerIds list of player ids that won the game (optional, use winningAlignment if not
 *         applicable)
 * @param participants list of player participations in this game
 * @param name human-readable name of this game
 */
@Schema(
        name = "GameCreationRequest",
        description = "Request object for creating a new game. Contains all information required to create a game."
)
public record GameCreationRequestDTO(
        @Schema(
                description = "Unique numerical identifier of the script (collection of characters) used in this game.",
                example = "5"
        ) Long scriptId,
        @Schema(
                description = "Free-form description of the game.",
                example = "A very intense game with lots of bluffing! The good team won due to the great play of the "
                        + "Fortune Teller."
        ) String description,
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
        ) PlayerParticipationDTO[] participants,
        @Schema(
                description = "Human-readable name of this game.",
                example = "Tom's birthday first game"
        ) String name
) implements Validateable {

    @Override
    public ValidationResult validate() {
        return new ValidationChain(
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
                Arrays.stream(winningPlayerIds).filter(playerId -> !participatingPlayers.contains(playerId)).toList();
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
        GameCreationRequestDTO that = (GameCreationRequestDTO) o;
        return Objects.equals(scriptId, that.scriptId) && Objects.equals(description, that.description)
                && winningAlignment == that.winningAlignment && Objects.deepEquals(winningPlayerIds,
                that.winningPlayerIds)
                && Objects.deepEquals(participants, that.participants) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scriptId, description, winningAlignment, Arrays.hashCode(winningPlayerIds),
                Arrays.hashCode(participants), name);
    }

    @Override
    public String toString() {
        return "GameCreationRequestDTO{"
                + "scriptId=" + scriptId
                + ", description='" + description + '\''
                + ", winningAlignment=" + winningAlignment
                + ", winningPlayerIds=" + Arrays.toString(winningPlayerIds)
                + ", participants=" + Arrays.toString(participants)
                + ", name='" + name + '\''
                + '}';
    }
}
