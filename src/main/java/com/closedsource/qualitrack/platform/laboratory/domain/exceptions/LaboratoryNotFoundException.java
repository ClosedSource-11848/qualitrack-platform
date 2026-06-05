package com.closedsource.qualitrack.platform.laboratory.domain.exceptions;

/**
 * Exception thrown when a laboratory is not found.
 * @summary
 * This exception is thrown when a laboratory is not found in the database.
 * @see RuntimeException
 */
public class LaboratoryNotFoundException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param laboratoryId The ID of the laboratory that was not found.
     */
    public LaboratoryNotFoundException(String laboratoryId) {
        super(String.format("Laboratory with ID %s not found.", laboratoryId));
    }
}