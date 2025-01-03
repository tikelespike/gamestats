package com.tikelespike.gamestats.businesslogic.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A script is a collection of characters that may appear in a game.
 */
public class Script {
    private final String name;
    private final String description;
    private final List<Character> characters;

    /**
     * Creates a new script with the given name, description, and characters.
     *
     * @param name the human-readable name of this script
     * @param description a free-form optional description of this script
     * @param characters the characters that may appear in a game when using this script
     */
    public Script(String name, String description, List<Character> characters) {
        this.name = name;
        this.description = description;
        this.characters = characters;
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
     * Returns a free-form optional description of this script.
     *
     * @return a human-readable description of this script. May be empty.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the characters that may appear in a game when using this script.
     *
     * @return the characters that may appear in a game when using this script
     */
    public List<Character> getCharacters() {
        return new ArrayList<>(characters);
    }
}
