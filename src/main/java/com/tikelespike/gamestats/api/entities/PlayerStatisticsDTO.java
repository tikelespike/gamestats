package com.tikelespike.gamestats.api.entities;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * REST transfer object for the statistics about a player.
 *
 * @param playerId unique numerical identifier of the player whose statistics are represented here
 * @param totalGamesPlayed total number of games this player has participated in either as player or as
 *         storyteller
 * @param totalWins total number of games this player has won
 * @param timesStoryteller number of games this player has acted as storyteller for
 * @param timesTownsfolk number of games this player has played as a townsfolk character, either as initial or
 *         final character
 * @param timesOutsider number of games this player has played as an outsider character, either as initial or
 *         final character
 * @param timesMinion number of games this player has played as a minion character, either as initial or final
 *         character
 * @param timesDemon number of games this player has played as a demon character, either as initial or final
 *         character
 * @param timesDeadAtEnd number of games where the player was dead at the end of the game
 * @param timesGood number of games where the player was on the good team at the end
 * @param timesEvil number of games where the player was on the evil team at the end
 * @param characterPlayingCounts a map mapping character id to the number of games this player has played that
 *         character (beginning or end)
 */
public record PlayerStatisticsDTO(
        @Schema(
                description = "Unique numerical identifier of the player whose statistics are represented"
                        + " by this object. Is not null.",
                example = "42"
        ) long playerId,
        @Schema(
                description = "Total number of games this player has participated in either as player or as "
                        + "storyteller.",
                example = "100"
        ) long totalGamesPlayed,
        @Schema(
                description = "Total number of games this player has won.",
                example = "50"
        ) long totalWins,
        @Schema(
                description = "Number of games this player has acted as storyteller for.",
                example = "10"
        ) long timesStoryteller,
        @Schema(
                description = "Number of games this player has played as a townsfolk character, either as initial or "
                        + "final character.",
                example = "30"
        ) long timesTownsfolk,
        @Schema(
                description = "Number of games this player has played as an outsider character, either as initial or "
                        + "final character.",
                example = "20"
        ) long timesOutsider,
        @Schema(
                description = "Number of games this player has played as a minion character, either as initial or "
                        + "final character.",
                example = "15"
        ) long timesMinion,
        @Schema(
                description = "Number of games this player has played as a demon character, either as initial or "
                        + "final character.",
                example = "5"
        ) long timesDemon,
        @Schema(
                description = "Number of games where the player was dead at the end of the game.",
                example = "25"
        ) long timesDeadAtEnd,
        @Schema(
                description = "Number of games where the player was on the good team at the end.",
                example = "40"
        ) long timesGood,
        @Schema(
                description = "Number of games where the player was on the evil team at the end.",
                example = "30"
        ) long timesEvil,
        @Schema(
                description = "Map mapping character id to the number of games this player has played that character "
                        + "(beginning or end).",
                example = "{\"1\": 10, \"2\": 5}"
        ) Map<Long, Long> characterPlayingCounts
) {
}
