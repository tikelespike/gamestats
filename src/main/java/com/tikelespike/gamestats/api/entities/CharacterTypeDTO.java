package com.tikelespike.gamestats.api.entities;

/**
 * REST transfer object of the character type.
 */
public enum CharacterTypeDTO {
    // note: enum values here violate the ALL_CAPS naming convention on purpose, since they get directly mapped to
    // strings in JSON, which should be lower case.

    /**
     * Townsfolk usually make up the majority of good players in the game, and almost always have abilities helpful to
     * the good team.
     */
    townsfolk,

    /**
     * The good team usually has some outsiders, whose abilities can be potentially harmful to the good team.
     */
    outsider,

    /**
     * Minions support the demon in the evil team, and try to deceive the townsfolk and outsiders.
     */
    minion,

    /**
     * The demon is the leader of the evil team, and usually kills one or more players in the night.
     */
    demon,

    /**
     * Travellers are a special case for players that might join late or leave early. They can be good or evil.
     */
    traveller
}
