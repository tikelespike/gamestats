package com.tikelespike.gamestats.api.entities;


/**
 * REST transfer object of a character (when retrieved or updated).
 *
 * @param id unique numerical identifier
 * @param name human-readable display name of the character
 * @param scriptToolIdentifier identifier as used in the official script tool
 * @param type type of the character, e.g. "townsfolk"
 * @param wikiPageLink full URL to the wiki page of that character (optional)
 */
public record CharacterDTO(
        Long id,
        String name,
        String scriptToolIdentifier,
        CharacterTypeDTO type,
        String wikiPageLink
) {
}
