package com.tikelespike.gamestats.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Database representation of a script. A script is a collection of characters that may appear in a game when using this
 * script.
 */
@Entity(name = "scripts")
public class ScriptEntity extends AbstractEntity {
    private String name;
    private String wikiPageLink;
    private String description;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "script_characters",
            joinColumns = @JoinColumn(name = "script_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "character_id",
                    foreignKey = @ForeignKey(
                            foreignKeyDefinition = "FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE "
                                    + "CASCADE"
                    )
            )
    )
    private List<CharacterEntity> characters;

    /**
     * Creates a new script entity with uninitialized fields. This constructor is used by the JPA provider to create a
     * new instance of this entity from the database.
     */
    protected ScriptEntity() {
    }

    /**
     * Creates a new script entity.
     *
     * @param id unique identifier of the script
     * @param version version counter for optimistic locking
     * @param name human-readable name of the script
     * @param wikiPageLink URL of the wiki page associated with this script (optional)
     * @param description a free-form optional description of this script (optional)
     * @param characters the characters that may appear in a game when using this script
     */
    public ScriptEntity(Long id, Long version, String name, String wikiPageLink, String description,
                        List<CharacterEntity> characters) {
        super(id, version);
        this.name = name;
        this.wikiPageLink = wikiPageLink;
        this.description = description;
        this.characters = new ArrayList<>(characters);
    }

    /**
     * Sets the display name of this script. This method is used by the JPA provider to set the name of this script when
     * loading it from the database.
     *
     * @param name display name of this script
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the URL of the wiki page associated with this script. This method is used by the JPA provider to set the URL
     * of the wiki page when loading this script from the database.
     *
     * @param wikiPageUrl URL of the wiki page associated with this script (optional)
     */
    protected void setWikiPageLink(String wikiPageUrl) {
        this.wikiPageLink = wikiPageUrl;
    }

    /**
     * Sets the description of this script. This method is used by the JPA provider to set the description of this
     * script when loading it from the database.
     *
     * @param description free-form optional description of this script (optional)
     */
    protected void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the characters that may appear in a game when using this script. This method is used by the JPA provider to
     * set the characters of this script when loading it from the database.
     *
     * @param characters set of characters that may appear in a game when using this script. May not be null or
     *         empty.
     */
    protected void setCharacters(List<CharacterEntity> characters) {
        this.characters = new ArrayList<>(characters);
    }

    /**
     * Returns the display name of this script.
     *
     * @return the display name of this script
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the URL of the wiki page associated with this script, if it exists.
     *
     * @return the URL of the wiki page associated with this script. May be null if no wiki page is associated.
     */
    public String getWikiPageLink() {
        return wikiPageLink;
    }

    /**
     * Returns a free-form optional description of this script.
     *
     * @return the description of this script. May be empty or null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the characters that may appear in a game when using this script.
     *
     * @return the characters that may appear in a game when using this script
     */
    public List<CharacterEntity> getCharacters() {
        return new ArrayList<>(characters);
    }

    /**
     * Removes a character from this script.
     *
     * @param character the character to remove from this script
     */
    public void removeCharacter(CharacterEntity character) {
        characters.remove(character);
    }

}
