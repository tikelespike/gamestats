package com.tikelespike.gamestats.businesslogic.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A script is a collection of characters that may appear in a game.
 */
public class Script implements HasId, HasWikiPage, HasVersion {

    private final Long id;
    private final Long version;
    private String wikiPageLink;
    private String name;
    private String description;
    private List<Character> characters;

    /**
     * Creates a new script with the given name, description, and characters.
     *
     * @param id unique identifier for this script. May not be null.
     * @param version version counter for optimistic locking. May not be null.
     * @param wikiPageLink URL of the wiki page associated with this script (optional)
     * @param name the human-readable name of this script. May not be null or blank.
     * @param description a free-form optional description of this script (optional)
     * @param characters the characters that may appear in a game when using this script. May not be null or
     *         empty.
     */
    public Script(Long id, Long version, String wikiPageLink, String name, String description,
                  List<Character> characters) {
        this.id = Objects.requireNonNull(id);
        this.version = Objects.requireNonNull(version);
        this.wikiPageLink = wikiPageLink;
        setName(name);
        setDescription(description);
        setCharacters(characters);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public String getWikiPageLink() {
        return wikiPageLink;
    }

    /**
     * Sets the URL of the wiki page associated with this script.
     *
     * @param wikiPageLink URL of the wiki page associated with this script (optional)
     */
    public void setWikiPageLink(String wikiPageLink) {
        this.wikiPageLink = wikiPageLink;
    }

    /**
     * Returns the human-readable name of this script.
     *
     * @return the human-readable name of this script
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the human-readable name of this script.
     *
     * @param name the human-readable name of this script. May not be null or blank.
     */
    public void setName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        this.name = name;
    }

    /**
     * Returns a free-form optional description of this script.
     *
     * @return a human-readable description of this script. May be empty.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a free-form optional description of this script.
     *
     * @param description a human-readable description of this script. May be empty.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the characters that may appear in a game when using this script.
     *
     * @return the characters that may appear in a game when using this script
     */
    public List<Character> getCharacters() {
        return new ArrayList<>(characters);
    }

    /**
     * Sets the characters that may appear in a game when using this script.
     *
     * @param characters the characters that may appear in a game when using this script. May not be null or
     *         empty.
     */
    public void setCharacters(List<Character> characters) {
        if (characters.isEmpty()) {
            throw new IllegalArgumentException("Characters must not be empty");
        }
        this.characters = new ArrayList<>(characters);
    }

}
