package com.tikelespike.gamestats.api.validation.checks;

import com.tikelespike.gamestats.api.validation.ValidationResult;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Checks if at least one of the specified fields is set. The fields are considered set if their value is not null and
 * not an empty string.
 */
public class EitherFieldRequiredCheck implements ValidationCheck {

    private static final String MESSAGE = "At least one of the following fields must be set: %s.";
    private final Field[] fields;

    /**
     * Creates a new check validating if at least one of the giving fields is non-null and non-blank.
     *
     * @param fields the fields to check
     */
    public EitherFieldRequiredCheck(Field... fields) {
        this.fields = fields;
    }

    @Override
    public ValidationResult validate() {
        if (Arrays.stream(fields)
                .allMatch(f -> f.value == null || f.value instanceof String strValue && strValue.isBlank())) {
            String missingFields =
                    Arrays.stream(fields).map(f -> "\"%s\"".formatted(f.name)).collect(Collectors.joining(", "));
            return ValidationResult.invalid(MESSAGE.formatted(missingFields));
        }
        return ValidationResult.valid();
    }

    /**
     * A single field to check. The field is considered set if its value is not null and not an empty string.
     *
     * @param name identifier of the field
     * @param value value of the field
     */
    public record Field(
            String name,
            Object value
    ) {
    }
}
