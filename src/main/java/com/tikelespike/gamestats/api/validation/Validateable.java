package com.tikelespike.gamestats.api.validation;

/**
 * An interface for objects that can be validated. This is typically used for transfer objects (DTOs) that are received
 * from the client and need to be validated before being processed by the server.
 */
public interface Validateable {

    /**
     * Validates this transfer object, checking that the data is valid and complete as long as dependent system state is
     * not considered.
     *
     * @return a ValidationResult object containing the result of the validation
     */
    ValidationResult validate();

}
