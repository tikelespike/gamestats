package com.tikelespike.gamestats.api.validation.checks;

import com.tikelespike.gamestats.api.validation.ValidationResult;

/**
 * A condition that must be met for a certain entity to be considered valid. If the condition is not met, a message
 * describing the reason for the failure can be provided.
 */
public interface ValidationCheck {

    /**
     * Validates the condition. If the condition is met, a valid result is returned. If the condition is not met, an
     * invalid result is returned with a message describing the reason for the failure.
     *
     * @return the result of the validation check
     */
    ValidationResult validate();
}
