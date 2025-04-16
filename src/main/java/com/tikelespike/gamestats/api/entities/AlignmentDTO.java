package com.tikelespike.gamestats.api.entities;

/**
 * REST transfer object for the alignment of a player or team.
 */
public enum AlignmentDTO {
    // note: enum values here violate the ALL_CAPS naming convention on purpose, since they get directly mapped to
    // strings in JSON, which should be lower case.

    /**
     * The good alignment, usually consisting of townsfolk and outsiders.
     */
    good,

    /**
     * The evil alignment, usually consisting of minions and a demon.
     */
    evil
}
