package com.tikelespike.gamestats.businesslogic.entities;

/**
 * Encapsulates all data necessary to create a new character.
 *
 * @param scriptToolIdentifier string identifier as used in the official script tool
 * @param name display name of the character
 * @param characterType type of the character (e.g. townsfolk)
 * @param wikiPageLink full URL to the wiki page of that character (optional)
 */
public record CharacterCreationRequest(
        String scriptToolIdentifier,
        String name,
        CharacterType characterType,
        String wikiPageLink
) {
}
