package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for the character creation request.
 *
 * @param name display name of the new character
 * @param scriptToolIdentifier identifier as used in the official script tool
 * @param type transfer object of the type the character is of (e.g. townsfolk)
 * @param wikiPageLink full URL to the wiki page of that character (optional)
 * @param imageUrl full URL to an image representing this character (optional)
 */
@Schema(
        name = "Character (creation)",
        description = "Request to create a character. A character is a role players can take on in a game, giving "
                + "them abilities."
)
public record CharacterCreationDTO(
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
        ) String wikiPageLink,
        @Schema(
                description = "Full URL to an image representing this character (optional).",
                example = "https://example.com/fortuneteller.png"
        ) String imageUrl
) {
    /**
     * Validates this character creation request.
     *
     * @return the validation result of this character creation request
     */
    public ValidationResult validate() {
        return new ValidationChain(
                new RequiredFieldCheck("name", name),
                new RequiredFieldCheck("type", type)
        ).validate();
    }
}
