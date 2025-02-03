package com.tikelespike.gamestats.businesslogic.entities;

import java.util.Objects;

/**
 * A character is the role that a player takes on in a game.  Players are assigned a character at the start of the game,
 * but it may change during the game due to various game mechanics.
 */
public class Character implements HasWikiPage, HasId {

    private final Long id;
    private final String scriptToolIdentifier;
    private final String name;
    private final CharacterType characterType;
    private final String wikiPageLink;

    /**
     * Creates a new character.
     *
     * @param id unique identifier for this character. May not be null.
     * @param scriptToolIdentifier identifier used in the official script tool for this character, if it exists
     *         there (optional). Must be unique among all characters.
     * @param name display name of this character (e.g. {@code "Fortune Teller"})
     * @param characterType the group of characters this one belongs to
     * @param wikiPageLink URL of the wiki page associated with this character (optional)
     */
    public Character(Long id, String scriptToolIdentifier, String name, CharacterType characterType,
                     String wikiPageLink) {
        this.id = Objects.requireNonNull(id);
        this.scriptToolIdentifier = scriptToolIdentifier;
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        this.name = name;
        this.characterType = Objects.requireNonNull(characterType);
        this.wikiPageLink = wikiPageLink;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * Returns the identifier used in the <a href="https://script.bloodontheclocktower.com/">official script tool</a>
     * for this character, if it exists there. At the time of writing, this seems to be the name of the character in
     * lowercase with spaces removed (e.g. {@code "fortuneteller"}).
     * <p>
     * This allows the creation of scripts based on JSON exports of the official script tool.
     *
     * @return the identifier used in the official script tool for this character
     */
    public String getScriptToolIdentifier() {
        return scriptToolIdentifier;
    }

    /**
     * Returns the display name of this character (e.g. {@code "Fortune Teller"}).
     *
     * @return the display name of this character
     */
    public String getName() {
        return name;
    }

    /**
     * Returns which group of characters this one belongs to. Each character belongs to exactly one type.
     *
     * @return the character type this character belongs to
     */
    public CharacterType getCharacterType() {
        return characterType;
    }

    @Override
    public String getWikiPageLink() {
        return wikiPageLink;
    }
}
