package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.Validateable;
import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for a request to create a new script.
 *
 * @param name human-readable display name for the script
 * @param description free-form description of the script (optional)
 * @param wikiPageLink full URL to the wiki page of the script (optional)
 * @param characterIds numerical ids of all characters used in the script
 */
@Schema(
        name = "Script (creation)",
        description = "Request to create a script. A script is a collection of characters defining which characters "
                + "may be in play during a game."
)
public record ScriptCreationDTO(
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
) implements Validateable {
    @Override
    public ValidationResult validate() {
        return new ValidationChain(
                new RequiredFieldCheck("name", name)
        ).validate();
    }
}
