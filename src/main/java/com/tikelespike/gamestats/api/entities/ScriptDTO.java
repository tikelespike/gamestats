package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.MatchingIdCheck;
import com.tikelespike.gamestats.api.validation.checks.MaxLengthCheck;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for a script (a collection of characters).
 *
 * @param id unique numerical identifier
 * @param version version counter for optimistic locking
 * @param name human-readable display name for the script
 * @param description free-form description of the script (optional)
 * @param wikiPageLink full URL to the wiki page of the script (optional)
 * @param characterIds numerical ids of all characters used in the script
 */
@Schema(
        name = "Script",
        description = "A collection of characters defining which characters may be in play during a game."
)
public record ScriptDTO(
        @Schema(
                description = "Unique numerical identifier of the script.",
                example = "42"
        ) Long id,
        @Schema(
                description = "Version counter for optimistic locking.",
                example = "1"
        ) Long version,
        @Schema(
                description = "Display name of the script.",
                example = "Trouble Brewing"
        ) String name,
        @Schema(
                description = "Free-form description of the script.",
                example = "Trouble Brewing has a little bit of everything."
        ) String description,
        @Schema(
                description = "Full URL to a wiki page about this script (optional).",
                example = "https://wiki.bloodontheclocktower.com/Trouble_Brewing"
        ) String wikiPageLink,
        @Schema(
                description = "List of characters that may appear in a game when using this script.",
                example = "[2, 3, 7]"
        ) Long[] characterIds
) {
    private static final int MAX_DESCRIPTION_LENGTH = 5000;

    /**
     * Validates this DTO in the context of updating a script.
     *
     * @param pathId id provided in the resource URI
     *
     * @return result of the validation
     */
    public ValidationResult validateUpdate(Long pathId) {
        return new ValidationChain(
                new RequiredFieldCheck("id", id),
                new MatchingIdCheck(pathId, id),
                new RequiredFieldCheck("version", version),
                new RequiredFieldCheck("name", name),
                new MaxLengthCheck("description", description, MAX_DESCRIPTION_LENGTH)
        ).validate();
    }
}
