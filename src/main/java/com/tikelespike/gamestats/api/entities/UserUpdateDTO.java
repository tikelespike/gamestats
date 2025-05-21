package com.tikelespike.gamestats.api.entities;

import com.tikelespike.gamestats.api.validation.ValidationChain;
import com.tikelespike.gamestats.api.validation.ValidationResult;
import com.tikelespike.gamestats.api.validation.checks.MatchingIdCheck;
import com.tikelespike.gamestats.api.validation.checks.RequiredFieldCheck;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST transfer object for updating a user.
 *
 * @param id unique numerical identifier
 * @param version version counter for optimistic locking
 * @param name user's human-readable name (preferably real-world name)
 * @param email user's email address
 * @param password new password (optional, only included if password should be changed)
 * @param permissionLevel permission level of the user
 */
@Schema(
        name = "User Update",
        description = "Data for updating an existing user. All fields except password are required. Password is only "
                + "included if it should be changed."
)
public record UserUpdateDTO(
        Long id,
        Long version,
        String name,
        String email,
        String password,
        UserRoleDTO permissionLevel
) {
    /**
     * Validates this DTO in the context of updating a user.
     *
     * @return a validation result indicating whether the DTO is valid or not
     */
    public ValidationResult validateUpdate(Long pathId) {
        return new ValidationChain(
                new RequiredFieldCheck("id", id),
                new MatchingIdCheck(pathId, id),
                new RequiredFieldCheck("version", version),
                new RequiredFieldCheck("name", name),
                new RequiredFieldCheck("email", email),
                new RequiredFieldCheck("permissionLevel", permissionLevel)
        ).validate();
    }
}
