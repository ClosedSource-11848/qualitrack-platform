package com.closedsource.qualitrack.platform.laboratory.domain.exceptions;

/**
 * Exception thrown when a raw material is not found.
 * @summary
 * This exception is thrown when a raw material is not found in the database.
 * @see RuntimeException
 */
public class RawMaterialNotFoundException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param rawMaterialId The ID of the raw material that was not found.
     */
    public RawMaterialNotFoundException(String rawMaterialId) {
        super(String.format("Raw material with ID %s not found.", rawMaterialId));
    }
}