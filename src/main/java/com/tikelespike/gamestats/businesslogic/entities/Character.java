package com.tikelespike.gamestats.businesslogic.entities;

/**
 * A character is the role that a player takes on in a game.  Players are assigned a character at the start of the game,
 * but it may change during the game due to various game mechanics.
 */
public interface Character extends HasWikiPage {

    /**
     * Returns a unique identifier for this character. This identifier should be human-readable, but not necessarily
     * suitable for display to the user. It should be unique within the context of the game system and, if possible,
     * match the identifier used in the <a href="https://script.bloodontheclocktower.com/">official script tool</a>. At
     * the time of writing, this seems to be the name of the character in lowercase with spaces removed (e.g.
     * {@code "fortuneteller"}).
     *
     * @return a unique identifier for this character
     */
    String getIdentifier();

    /**
     * Returns the display name of this character (e.g. {@code "Fortune Teller"}).
     *
     * @return the display name of this character
     */
    String getName();

    /**
     * Returns which group of characters this one belongs to. Each character belongs to exactly one type.
     *
     * @return the character type this character belongs to
     */
    CharacterType getCharacterType();

}
