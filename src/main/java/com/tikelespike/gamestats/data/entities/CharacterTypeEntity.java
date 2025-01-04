package com.tikelespike.gamestats.data.entities;

/**
 * Database representation of character types.
 */
public enum CharacterTypeEntity {
    /**
     * Townsfolk usually make up the majority of good players in the game, and almost always have abilities helpful to
     * the good team.
     */
    TOWNSFOLK,

    /**
     * The good team usually has some outsiders, whose abilities can be potentially harmful to the good team.
     */
    OUTSIDER,

    /**
     * Minions support the demon in the evil team, and try to deceive the townsfolk and outsiders.
     */
    MINION,

    /**
     * The demon is the leader of the evil team, and usually kills one or more players in the night.
     */
    DEMON,

    /**
     * Travellers are a special case for players that might join late or leave early. They can be good or evil.
     */
    TRAVELLER
}
