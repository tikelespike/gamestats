package com.tikelespike.gamestats.api.entities;


import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.MatchingIdCheck;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object of a character (when retrieved or updated).
 *
 * @param id unique numerical identifier
 * @param version version counter for optimistic locking
 * @param name human-readable display name of the character
 * @param scriptToolIdentifier identifier as used in the official script tool
 * @param type type of the character, e.g. "townsfolk"
 * @param wikiPageLink full URL to the wiki page of that character (optional)
 */
@Schema(
        name = "Character",
        description = "A character players can take on in a game to get abilities."
)
public record CharacterDTO(
        @Schema(
                description = "Unique numerical identifier of the character.",
                example = "42"
        ) Long id,
        @Schema(
                description = "Version counter for optimistic locking.",
                example = "1"
        ) Long version,
        @Schema(
                description = "Display name of the character.",
                example = "Fortune Teller"
        ) String name,
        @Schema(
                description = "Identifier as recognized by the official Blood on the Clocktower script generation "
                        + "tool. Optional, but enables import and export of scripts from/to said tool.",
                example = "fortuneteller"
        ) String scriptToolIdentifier,
        @Schema(
                description = "Subgroup this character belongs to (lowercase).",
                example = "townsfolk"
        ) CharacterTypeDTO type,
        @Schema(
                description = "Full URL to a wiki page about this character (optional).",
                example = "https://wiki.bloodontheclocktower.com/Fortune_Teller"
        ) String wikiPageLink
) {
    /**
     * Validates this DTO in the context of updating a character.
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
                new RequiredFieldCheck("type", type)
        ).validate();
    }
}
