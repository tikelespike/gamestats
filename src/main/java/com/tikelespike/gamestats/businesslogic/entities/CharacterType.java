package com.tikelespike.gamestats.businesslogic.entities;

/**
 * Characters in this game are grouped into types - townsfolk and outsiders, which are (usually) good, and minions and
 * demons, which are (usually) evil. Travellers are a special case for players that might join late or leave early, as
 * they can be good or evil.
 */
public enum CharacterType {
    /**
     * Townsfolk usually make up the majority of good players in the game, and almost always have abilities helpful to
     * the good team.
     */
    TOWNSFOLK(Alignment.GOOD),
    /**
     * The good team usually has some outsiders, whose abilities can be potentially harmful to the good team.
     */
    OUTSIDER(Alignment.GOOD),
    /**
     * Minions support the demon in the evil team, and try to deceive the townsfolk and outsiders.
     */
    MINION(Alignment.EVIL),
    /**
     * The demon is the leader of the evil team, and usually kills one or more players in the night.
     */
    DEMON(Alignment.EVIL),
    /**
     * Travellers are a special case for players that might join late or leave early. They can be good or evil.
     */
    TRAVELLER(Alignment.GOOD);

    private final Alignment defaultAlignment;

    CharacterType(Alignment defaultAlignment) {
        this.defaultAlignment = defaultAlignment;
    }

    /**
     * Returns the alignment (team) that a character of this type is usually on. While this is true more often than not,
     * players can change alignments during the game due to various effects. However, by default, townsfolk and
     * outsiders are good, and minions and demons are evil.
     *
     * @return the default alignment for characters of this type
     */
    public Alignment getDefaultAlignment() {
        return defaultAlignment;
    }
}
