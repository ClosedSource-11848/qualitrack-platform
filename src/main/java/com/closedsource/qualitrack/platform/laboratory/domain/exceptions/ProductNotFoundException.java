package com.closedsource.qualitrack.platform.laboratory.domain.exceptions;

/**
 * Exception thrown when a pharmaceutical product is not found.
 * @summary
 * This exception is thrown when a pharmaceutical product is not found in the database.
 * @see RuntimeException
 */
public class ProductNotFoundException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param productId The ID of the product that was not found.
     */
    public ProductNotFoundException(String productId) {
        super(String.format("Pharmaceutical product with ID %s not found.", productId));
    }
}