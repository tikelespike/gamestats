package com.tikelespike.gamestats.data.entities;

/**
 * Database representation of user roles. Every user should have the USER role. The other roles grant additional
 * permissions suitable for different types of users.
 */
public enum UserRoleEntity {
    /**
     * A regular user with no special permissions. This role should be granted to every registered user.
     */
    USER,

    /**
     * A user that can create and manage games.
     */
    STORYTELLER,

    /**
     * A user that can manage other users and their roles.
     */
    ADMIN
}
