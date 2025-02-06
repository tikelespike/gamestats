package com.tikelespike.gamestats.api.validation;

/**
 * Represents the result of a validation check. Can either be valid or invalid. If invalid, a message can be provided to
 * describe the reason for the failure.
 */
public final class ValidationResult {

    private static final ValidationResult VALID = new ValidationResult(true, null);

    private final boolean isValid;
    private final String message;

    /**
     * A valid result without a message.
     *
     * @return a result object representing successful validation
     */
    public static ValidationResult valid() {
        return VALID;
    }

    /**
     * An invalid result with a message.
     *
     * @param message the message to be included in the result
     *
     * @return a result object representing failed validation
     */
    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }


    private ValidationResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    /**
     * Returns whether the validation was successful.
     *
     * @return true iff. the validation was successful
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Returns a message describing why the validation was not successful (if it failed).
     *
     * @return the error message if the validation was not successful, otherwise null
     */
    public String getMessage() {
        return message;
    }
}
