package com.tikelespike.gamestats.businesslogic.entities;

import java.util.Objects;

/**
 * Encapsulates all data necessary to create a new character.
 *
 * @param scriptToolIdentifier string identifier as used in the official script tool (optional)
 * @param name display name of the character. May not be null or blank.
 * @param characterType type of the character (e.g. townsfolk). May not be null.
 * @param wikiPageLink full URL to the wiki page of that character (optional)
 */
public record CharacterCreationRequest(
        String scriptToolIdentifier,
        String name,
        CharacterType characterType,
        String wikiPageLink
) {

    /**
     * Creates a new request for character creation.
     *
     * @param scriptToolIdentifier string identifier as used in the official script tool (optional)
     * @param name display name of the character. May not be null or blank.
     * @param characterType type of the character (e.g. townsfolk). May not be null.
     * @param wikiPageLink full URL to the wiki page of that character (optional)
     */
    public CharacterCreationRequest(
            String scriptToolIdentifier,
            String name,
            CharacterType characterType,
            String wikiPageLink
    ) {
        this.scriptToolIdentifier = scriptToolIdentifier;
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        this.name = name;
        this.characterType = Objects.requireNonNull(characterType);
        this.wikiPageLink = wikiPageLink;
    }

}
