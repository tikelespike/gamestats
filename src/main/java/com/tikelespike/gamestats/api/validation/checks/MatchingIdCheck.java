package com.tikelespike.gamestats.api.validation.checks;

import com.tikelespike.gamestats.api.validation.ValidationResult;

/**
 * A check that validates that the identifier provided in the body matches the identifier obtained from the resource
 * URI.
 * <p>
 * If no identifier is provided in the body, the check is considered to be successful. If you want to ensure that the id
 * is present, consider the {@link RequiredFieldCheck}.
 */
public class MatchingIdCheck implements ValidationCheck {

    private static final String ERROR_MESSAGE = "Identifier provided in body does not match resource URI";
    private final Long pathId;
    private final Long bodyId;

    /**
     * Creates a new check that validates if the ids provided in the URI path and the body match.
     *
     * @param pathId identifier obtained from the resource URI
     * @param bodyId identifier provided in the body
     */
    public MatchingIdCheck(Long pathId, Long bodyId) {
        this.pathId = pathId;
        this.bodyId = bodyId;
    }

    @Override
    public ValidationResult validate() {
        if (bodyId != null && !bodyId.equals(pathId)) {
            return ValidationResult.invalid(ERROR_MESSAGE);
        }
        return ValidationResult.valid();
    }
}
