package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


/**
 * Database representation of a game character.
 */
@Entity(name = "characters")
public class CharacterEntity extends AbstractEntity {
    private String scriptToolIdentifier;
    private String name;

    @Enumerated(EnumType.STRING)
    private CharacterTypeEntity characterType;

    private String wikiPageLink;

    /**
     * Creates a new character entity with uninitialized fields. This constructor is used by the JPA provider to create
     * a new instance of this entity from the database.
     */
    protected CharacterEntity() {
    }

    /**
     * Creates a new character entity.
     *
     * @param id unique identifier of the character
     * @param scriptToolIdentifier unique identifier of the character in the script tool
     * @param name display name of the character
     * @param characterType the group of characters this one belongs to
     * @param wikiPageLink URL of the wiki page associated with this character
     */
    public CharacterEntity(Long id, String scriptToolIdentifier, String name, CharacterTypeEntity characterType,
                           String wikiPageLink) {
        super(id);
        this.scriptToolIdentifier = scriptToolIdentifier;
        this.name = name;
        this.characterType = characterType;
        this.wikiPageLink = wikiPageLink;
    }

    /**
     * Returns the identifier used in the official script tool for this character, if it exists there. At the time of
     * writing, this seems to be the name of the character in lowercase with spaces removed (e.g.
     * {@code "fortuneteller"}).
     *
     * @return the identifier used in the official script tool for this character
     */
    public String getScriptToolIdentifier() {
        return scriptToolIdentifier;
    }

    /**
     * Sets the identifier used in the official script tool for this character. This method is used by the JPA provider
     * to set the identifier of a new instance when it is loaded from the database. Should not be called by application
     * code.
     *
     * @param scriptToolIdentifier the new identifier used in the official script tool for this character
     */
    protected void setScriptToolIdentifier(String scriptToolIdentifier) {
        this.scriptToolIdentifier = scriptToolIdentifier;
    }

    /**
     * Returns the display name of this character.
     *
     * @return the display name of this character
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of this character. This method is used by the JPA provider to set the display name of an
     * instance when it is loaded from the database. Should not be called by application code.
     *
     * @param name the new display name of this character
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the group of characters this one belongs to.
     *
     * @return the group of characters this one belongs to
     */
    public CharacterTypeEntity getCharacterType() {
        return characterType;
    }

    /**
     * Sets the group of characters this one belongs to. This method is used by the JPA provider to set the group of
     * characters of a new instance when it is loaded from the database. Should not be called by application code.
     *
     * @param characterType the new group of characters this one belongs to
     */
    protected void setCharacterType(CharacterTypeEntity characterType) {
        this.characterType = characterType;
    }

    /**
     * Returns the URL of the wiki page associated with this character, if it exists.
     *
     * @return the URL of the wiki page associated with this character. May be null if no wiki page is associated.
     */
    public String getWikiPageLink() {
        return wikiPageLink;
    }

    /**
     * Sets the URL of the wiki page associated with this character. This method is used by the JPA provider to set the
     * URL of a new instance when it is loaded from the database. Should not be called by application code.
     *
     * @param wikiPageLink the new URL of the wiki page associated with this character
     */
    protected void setWikiPageLink(String wikiPageLink) {
        this.wikiPageLink = wikiPageLink;
    }
}
