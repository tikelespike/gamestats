package com.tikelespike.gamestats.businesslogic.entities;


/**
 * Statistical data associated with a single player's participation in a single game.
 *
 * @param player the player that participated in this game and whose data is represented by this object (may be
 *         null)
 * @param initialCharacter the initial character the player was assigned at the start of the game (may be null)
 * @param initialAlignment the alignment the player was assigned at the start of the game (if null and
 *         initialCharacter is non-null, defaults to the standard alignment of the initial character)
 * @param endCharacter the character the player ended the game with (if null and initialCharacter is non-null,
 *         defaults to the initial character)
 * @param endAlignment the alignment the player ended the game with (if null and endCharacter is non-null,
 *         defaults to the standard alignment of the end character)
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
     * @param player the player that participated in this game and whose data is represented by this object (may
     *         be null)
     * @param initialCharacter the initial character the player was assigned at the start of the game (may be
     *         null)
     * @param initialAlignment the alignment the player was assigned at the start of the game (if null and
     *         initialCharacter is non-null, defaults to the standard alignment of the initial character)
     * @param endCharacter the character the player ended the game with (if null and initialCharacter is
     *         non-null, defaults to the initial character)
     * @param endAlignment the alignment the player ended the game with (if null and endCharacter is non-null,
     *         defaults to the standard alignment of the end character)
     * @param isAliveAtEnd whether the player was still alive at the end of the game
     */
    public PlayerParticipation(Player player, Character initialCharacter, Alignment initialAlignment,
                               Character endCharacter, Alignment endAlignment, boolean isAliveAtEnd) {
        this.player = player;
        this.initialCharacter = initialCharacter;
        this.initialAlignment = or(initialAlignment,
                initialCharacter == null ? null : initialCharacter.getCharacterType().getDefaultAlignment());
        this.endCharacter = or(endCharacter, initialCharacter);
        this.endAlignment = or(endAlignment, this.endCharacter == null ? null
                : this.endCharacter.getCharacterType().getDefaultAlignment());
        this.isAliveAtEnd = isAliveAtEnd;
    }

    /**
     * Creates a new {@link PlayerParticipation} object with the given data, assuming that the player's character and
     * alignment did not change during the game.
     *
     * @param player the player that participated in this game and whose data is represented by this object (may
     *         be null)
     * @param character the character played by the player (may be null)
     * @param isAliveAtEnd whether the player was still alive at the end of the game
     */
    public PlayerParticipation(Player player, Character character, boolean isAliveAtEnd) {
        this(player, character, null, null, null, isAliveAtEnd);
    }

    private <T> T or(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
