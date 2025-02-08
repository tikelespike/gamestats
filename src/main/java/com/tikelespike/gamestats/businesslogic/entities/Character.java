package com.tikelespike.gamestats.businesslogic.entities;

import java.util.Objects;

/**
 * A character is the role that a player takes on in a game.  Players are assigned a character at the start of the game,
 * but it may change during the game due to various game mechanics.
 */
public class Character implements HasWikiPage, HasId, HasVersion {

    private final Long id;
    private final Long version;
    private String scriptToolIdentifier;
    private String name;
    private CharacterType characterType;
    private String wikiPageLink;

    /**
     * Creates a new character.
     *
     * @param id unique identifier for this character. May not be null.
     * @param version version counter for optimistic locking
     * @param scriptToolIdentifier identifier used in the official script tool for this character, if it exists
     *         there (optional). Should be unique among all characters.
     * @param name display name of this character (e.g. {@code "Fortune Teller"}). May not be null or blank.
     * @param characterType the group of characters this one belongs to. May not be null.
     * @param wikiPageLink URL of the wiki page associated with this character (optional)
     */
    public Character(Long id, Long version, String scriptToolIdentifier, String name, CharacterType characterType,
                     String wikiPageLink) {
        this.id = Objects.requireNonNull(id);
        this.version = version;
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
     * Sets the identifier used in the <a href="https://script.bloodontheclocktower.com/">official script tool</a> for
     * this character, if it exists there. At the time of writing, this seems to be the name of the character in
     * lowercase with spaces removed (e.g. {@code "fortuneteller"}).
     * <p>
     * This allows the creation of scripts based on JSON exports of the official script tool.
     *
     * @param scriptToolIdentifier identifier used in the official script tool for this character, if it exists
     *         there (optional). Should be unique among all characters.
     */
    public void setScriptToolIdentifier(String scriptToolIdentifier) {
        this.scriptToolIdentifier = scriptToolIdentifier;
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
     * Sets the display name of this character (e.g. {@code "Fortune Teller"}).
     *
     * @param name display name of this character (e.g. {@code "Fortune Teller"}). May not be null or blank.
     */
    public void setName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        this.name = name;
    }

    /**
     * Returns which group of characters this one belongs to. Each character belongs to exactly one type.
     *
     * @return the character type this character belongs to
     */
    public CharacterType getCharacterType() {
        return characterType;
    }

    /**
     * Sets which group of characters this one belongs to. Each character belongs to exactly one type.
     *
     * @param characterType the group of characters this one belongs to. May not be null.
     */
    public void setCharacterType(CharacterType characterType) {
        this.characterType = Objects.requireNonNull(characterType);
    }

    @Override
    public String getWikiPageLink() {
        return wikiPageLink;
    }

    /**
     * Sets the URL of the wiki page associated with this character.
     *
     * @param wikiPageLink URL of the wiki page associated with this character (optional)
     */
    public void setWikiPageLink(String wikiPageLink) {
        this.wikiPageLink = wikiPageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Character character = (Character) o;
        return Objects.equals(id, character.id) && Objects.equals(scriptToolIdentifier,
                character.scriptToolIdentifier) && Objects.equals(name, character.name)
                && characterType == character.characterType && Objects.equals(wikiPageLink,
                character.wikiPageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scriptToolIdentifier, name, characterType, wikiPageLink);
    }

    @Override
    public Long getVersion() {
        return version;
    }
}
