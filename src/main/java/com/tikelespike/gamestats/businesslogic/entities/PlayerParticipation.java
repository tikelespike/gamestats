package com.tikelespike.gamestats.businesslogic.entities;


import java.util.Objects;

/**
 * Statistical data associated with a single player's participation in a single game.
 *
 * @param player the player that participated in this game and whose data is represented by this object
 * @param initialCharacter the initial character the player was assigned at the start of the game
 * @param initialAlignment the alignment the player was assigned at the start of the game
 * @param endCharacter the character the player ended the game with
 * @param endAlignment the alignment the player ended the game with (relevant for the winning condition)
 * @param isAliveAtEnd whether the player was still alive at the end of the game
 */
public record PlayerParticipation(
        Player player,
        Character initialCharacter,
        Alignment initialAlignment,
        Character endCharacter,
        Alignment endAlignment,
        boolean isAliveAtEnd
) {
    /**
     * Creates a new {@link PlayerParticipation} object with the given data.
     *
     * @param player the player that participated in this game and whose data is represented by this object
     * @param initialCharacter the initial character the player was assigned at the start of the game
     * @param initialAlignment the alignment the player was assigned at the start of the game (defaults to the
     *         standard alignment of the initial character if null)
     * @param endCharacter the character the player ended the game with (defaults to the initial character if
     *         null)
     * @param endAlignment the alignment the player ended the game with (relevant for the winning condition,
     *         defaults to the standard alignment of the end character if null)
     * @param isAliveAtEnd whether the player was still alive at the end of the game
     */
    public PlayerParticipation(Player player, Character initialCharacter, Alignment initialAlignment,
                               Character endCharacter, Alignment endAlignment, boolean isAliveAtEnd) {
        this.player = Objects.requireNonNull(player);
        this.initialCharacter = Objects.requireNonNull(initialCharacter);
        this.initialAlignment = or(initialAlignment, initialCharacter.getCharacterType().getDefaultAlignment());
        this.endCharacter = or(endCharacter, initialCharacter);
        this.endAlignment = or(endAlignment, this.endCharacter.getCharacterType().getDefaultAlignment());
        this.isAliveAtEnd = isAliveAtEnd;
    }

    /**
     * Creates a new {@link PlayerParticipation} object with the given data, assuming that the player's character and
     * alignment did not change during the game.
     *
     * @param player the player that participated in this game and whose data is represented by this object
     * @param character the character played by the player
     * @param isAliveAtEnd whether the player was still alive at the end of the game
     */
    public PlayerParticipation(Player player, Character character, boolean isAliveAtEnd) {
        this(player, character, null, null, null, isAliveAtEnd);
    }

    private <T> T or(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
