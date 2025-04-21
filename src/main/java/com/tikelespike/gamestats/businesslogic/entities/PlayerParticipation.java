package com.tikelespike.gamestats.businesslogic.entities;


import java.util.Objects;

/**
 * Statistical data associated with a single player's participation in a single game.
 */
public final class PlayerParticipation {
    private final Player player;
    private final Character initialCharacter;
    private final Alignment initialAlignment;
    private final Character endCharacter;
    private final Alignment endAlignment;
    private final boolean isAliveAtEnd;

    /**
     * Creates a new {@link PlayerParticipation} object with the given data.
     *
     * @param player the player that participated in this game and whose data is represented by this object (may
     *         be null)
     * @param initialCharacter the initial character the player was assigned at the start of the game (may be
     *         null)
     * @param initialAlignment the alignment the player was assigned at the start of the game (may be null)
     * @param endCharacter the character the player ended the game with (may be null)
     * @param endAlignment the alignment the player ended the game with (may be null)
     * @param isAliveAtEnd whether the player was still alive at the end of the game
     */
    public PlayerParticipation(Player player, Character initialCharacter, Alignment initialAlignment,
                               Character endCharacter, Alignment endAlignment, boolean isAliveAtEnd) {
        this.player = player;
        this.initialCharacter = initialCharacter;
        this.initialAlignment = initialAlignment;
        this.endCharacter = endCharacter;
        this.endAlignment = endAlignment;
        this.isAliveAtEnd = isAliveAtEnd;
    }

    /**
     * Creates a new {@link PlayerParticipation} object with the given data, assuming that the player's character and
     * alignment did not change during the game, and the player's initial alignment is that of the character.
     *
     * @param player the player that participated in this game and whose data is represented by this object (may
     *         be null)
     * @param character the character played by the player (may be null)
     * @param isAliveAtEnd whether the player was still alive at the end of the game
     */
    public PlayerParticipation(Player player, Character character, boolean isAliveAtEnd) {
        this.player = player;
        this.initialCharacter = character;
        this.endCharacter = character;
        this.initialAlignment = character == null ? null : character.getCharacterType().getDefaultAlignment();
        this.endAlignment = initialAlignment;
        this.isAliveAtEnd = isAliveAtEnd;
    }

    /**
     * Returns the player whose game data is represented by this object.
     *
     * @return the player whose game data is represented by this object (may be null)
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the character the player started the game with.
     *
     * @return the character the player started the game with (may be null)
     */
    public Character getInitialCharacter() {
        return initialCharacter;
    }

    /**
     * Returns the alignment the player started the game with.
     *
     * @return the alignment the player started the game with (may be null)
     */
    public Alignment getInitialAlignment() {
        return initialAlignment;
    }

    /**
     * Returns the character the player ended the game with.
     *
     * @return the character the player ended the game with (may be null)
     */
    public Character getEndCharacter() {
        return endCharacter;
    }

    /**
     * Returns the alignment the player ended the game with.
     *
     * @return the alignment the player ended the game with (may be null)
     */
    public Alignment getEndAlignment() {
        return endAlignment;
    }

    /**
     * Returns whether the player was still alive when the game ended.
     *
     * @return true if and only if the player was alive at the time of the game's end
     */
    public boolean getIsAliveAtEnd() {
        return isAliveAtEnd;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (PlayerParticipation) obj;
        return Objects.equals(this.player, that.player)
                && Objects.equals(this.initialCharacter, that.initialCharacter)
                && Objects.equals(this.initialAlignment, that.initialAlignment)
                && Objects.equals(this.endCharacter, that.endCharacter)
                && Objects.equals(this.endAlignment, that.endAlignment)
                && this.isAliveAtEnd == that.isAliveAtEnd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, initialCharacter, initialAlignment, endCharacter, endAlignment, isAliveAtEnd);
    }

    @Override
    public String toString() {
        return "PlayerParticipation["
                + "player=" + player + ", "
                + "initialCharacter=" + initialCharacter + ", "
                + "initialAlignment=" + initialAlignment + ", "
                + "endCharacter=" + endCharacter + ", "
                + "endAlignment=" + endAlignment + ", "
                + "isAliveAtEnd=" + isAliveAtEnd + ']';
    }

}
