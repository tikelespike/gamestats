package com.tikelespike.gamestats.api.validation.checks;

import com.tikelespike.gamestats.api.validation.ValidationResult;

/**
 * Checks that a string does not exceed a maximum length.
 */
public class MaxLengthCheck implements ValidationCheck {

    private static final String FIELD_TOO_LONG_MESSAGE = "Field \"%s\" exceeds maximum length of %d characters";
    private final String fieldName;
    private final String fieldValue;
    private final int maxLength;

    /**
     * Creates a new check for a maximum length of a field.
     *
     * @param fieldName the name of the field to check
     * @param fieldValue the value of the field to check
     * @param maxLength maximum allowed length of the field value (inclusive)
     */
    public MaxLengthCheck(String fieldName, String fieldValue, int maxLength) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.maxLength = maxLength;
    }

    @Override
    public ValidationResult validate() {
        if (fieldValue != null && fieldValue.length() > maxLength) {
            return ValidationResult.invalid(FIELD_TOO_LONG_MESSAGE.formatted(fieldName, maxLength));
        }
        return ValidationResult.valid();
    }

}
