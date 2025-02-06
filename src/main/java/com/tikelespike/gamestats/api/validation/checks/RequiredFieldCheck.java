package com.tikelespike.gamestats.api.validation.checks;

import com.tikelespike.gamestats.api.validation.ValidationResult;

/**
 * Checks if a required field is missing.
 */
public class RequiredFieldCheck implements ValidationCheck {
    private static final String MISSING_FIELD_MESSAGE = "Missing required field \"%s\"";

    private final String fieldName;
    private final Object fieldValue;

    /**
     * Creates a new check for a required field.
     *
     * @param fieldName the name of the field to check
     * @param fieldValue the value of the field to check
     */
    public RequiredFieldCheck(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public ValidationResult validate() {
        if (fieldValue == null || fieldValue instanceof String strValue && strValue.isBlank()) {
            return ValidationResult.invalid(MISSING_FIELD_MESSAGE.formatted(fieldName));
        }
        return ValidationResult.valid();
    }
}
