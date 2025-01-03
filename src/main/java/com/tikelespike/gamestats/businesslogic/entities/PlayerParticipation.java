package com.tikelespike.gamestats.businesslogic.entities;


/**
 * Statistical data associated with a single player's participation in a single game.
 *
 * @param initialCharacter the initial character the player was assigned at the start of the game
 * @param endAlignment the alignment the player ended the game with (relevant for the winning condition)
 * @param isAliveAtEnd whether the player was still alive at the end of the game
 */
public record PlayerParticipation(
        Character initialCharacter,
        Alignment endAlignment,
        boolean isAliveAtEnd
) {
}
