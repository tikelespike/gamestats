package com.tikelespike.gamestats.businesslogic.entities;

import java.util.Set;

/**
 * Encapsulates all data needed to create a new script.
 *
 * @param name name of the new script (e.g. "Trouble Brewing")
 * @param description description of the new script
 * @param wikiPageLink full URL to the wiki page of that script (optional)
 * @param characters list of characters that may appear in a game when using this script
 */
public record ScriptCreationRequest(
        String name,
        String description,
        String wikiPageLink,
        Set<Character> characters
) {
    /**
     * Creates a new request for script creation.
     *
     * @param name name of the new script (e.g. "Trouble Brewing"). May not be null or blank.
     * @param description description of the new script (optional)
     * @param wikiPageLink full URL to the wiki page of that script (optional)
     * @param characters list of characters that may appear in a game when using this script. May not be null.
     */
    public ScriptCreationRequest {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        if (characters == null) {
            throw new IllegalArgumentException("Characters must not be null");
        }
        for (Character character : characters) {
            if (character == null) {
                throw new NullPointerException("Characters must be non-null");
            }
        }
    }
}
