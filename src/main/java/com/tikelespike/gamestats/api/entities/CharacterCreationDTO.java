package com.tikelespike.gamestats.api.entities;

/**
 * REST transfer object for the character creation request.
 *
 * @param name display name of the new character
 * @param scriptToolIdentifier identifier as used in the official script tool
 * @param type transfer object of the type the character is of (e.g. townsfolk)
 * @param wikiPageLink full URL to the wiki page of that character (optional)
 */
public record CharacterCreationDTO(
        String name,
        String scriptToolIdentifier,
        CharacterTypeDTO type,
        String wikiPageLink
) {
}
