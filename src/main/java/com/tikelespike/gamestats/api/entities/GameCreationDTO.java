package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.Validateable;
import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.EitherFieldRequiredCheck;
import com.tikelespike.gamestats.api.validation.checks.MaxLengthCheck;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * REST transfer object for creating a new game.
 *
 * @param name human-readable name of this game
 * @param description free-form description of the game (optional)
 * @param scriptId id of the script used in this game
 * @param winningAlignment the alignment that won the game (optional, use winningPlayerIds if not applicable)
 * @param winningPlayerIds list of player ids that won the game (optional, use winningAlignment if not
 *         applicable)
 * @param participants list of player participations in this game
 * @param storytellerIds list of player ids that acted as storytellers for this game
 */
@Schema(
        name = "GameCreationRequest",
        description = "Request object for creating a new game. Contains all information required to create a game."
)
public record GameCreationDTO(
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
                description = "List of player ids that acted as storytellers for this game.",
                example = "[4, 5]"
        ) Long[] storytellerIds,
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
) implements Validateable {

    private static final int MAX_DESCRIPTION_LENGTH = 5000;

    @Override
    public ValidationResult validate() {
        return new ValidationChain(
                new RequiredFieldCheck("participants", participants),
                new RequiredFieldCheck("name", name),
                new MaxLengthCheck("description", description, MAX_DESCRIPTION_LENGTH),
                this::checkAllParticipationsValid,
                this::checkNoDuplicatePlayersInParticipations,
                new EitherFieldRequiredCheck(
                        new EitherFieldRequiredCheck.Field("winningAlignment", winningAlignment),
                        new EitherFieldRequiredCheck.Field("winningPlayerIds", winningPlayerIds)
                ),
                this::checkWinningPlayerIdsValid,
                this::checkStorytellerIdsValid
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
                .filter(Objects::nonNull)
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
        Object[] playerIds = Arrays.stream(participants)
                .map(PlayerParticipationDTO::playerId)
                .filter(Objects::nonNull)
                .toArray();
        if (containsDuplicates(playerIds)) {
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

    private ValidationResult checkStorytellerIdsValid() {
        if (storytellerIds == null) {
            return ValidationResult.valid();
        }

        if (Arrays.stream(storytellerIds)
                .anyMatch(Objects::isNull)) {
            return ValidationResult.invalid("Storyteller ids must not contain null values.");
        }

        if (containsDuplicates(storytellerIds)) {
            return ValidationResult.invalid("The same player cannot be a storyteller multiple times in the same game.");
        }

        return ValidationResult.valid();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameCreationDTO that = (GameCreationDTO) o;
        return Objects.equals(scriptId, that.scriptId) && Objects.equals(description, that.description)
                && winningAlignment == that.winningAlignment && Objects.deepEquals(winningPlayerIds,
                that.winningPlayerIds)
                && Objects.deepEquals(participants, that.participants) && Objects.equals(name, that.name)
                && Objects.deepEquals(storytellerIds, that.storytellerIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scriptId, description, winningAlignment, Arrays.hashCode(winningPlayerIds),
                Arrays.hashCode(participants), name, Arrays.hashCode(storytellerIds));
    }

    @Override
    public String toString() {
        return "GameCreationDTO{"
                + "scriptId=" + scriptId
                + ", description='" + description + '\''
                + ", winningAlignment=" + winningAlignment
                + ", winningPlayerIds=" + Arrays.toString(winningPlayerIds)
                + ", participants=" + Arrays.toString(participants)
                + ", name='" + name + '\''
                + ", storytellerIds=" + Arrays.toString(storytellerIds)
                + '}';
    }
}
