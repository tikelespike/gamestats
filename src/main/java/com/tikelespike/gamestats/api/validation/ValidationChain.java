package com.tikelespike.gamestats.api.validation;

import com.tikelespike.gamestats.api.validation.checks.ValidationCheck;

import java.util.List;

/**
 * Represents a series of validation checks that are executed in order. If any of the checks fail, an invalid result is
 * returned. If all checks pass, a valid result is returned.
 */
public class ValidationChain {
    private final List<ValidationCheck> checks;

    /**
     * Creates a new validation chain with the given checks.
     *
     * @param checks the checks to be executed in order
     */
    public ValidationChain(List<ValidationCheck> checks) {
        this.checks = checks;
    }

    /**
     * Creates a new validation chain with the given checks.
     *
     * @param checks the checks to be executed in order
     */
    public ValidationChain(ValidationCheck... checks) {
        this.checks = List.of(checks);
    }

    /**
     * Validates the whole chain of checks. If any of the checks fail, an invalid result is returned. If all checks
     * pass, a valid result is returned.
     *
     * @return a result object representing the outcome of the validation
     */
    public ValidationResult validate() {
        for (ValidationCheck check : checks) {
            ValidationResult result = check.validate();
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.valid();
    }
}
