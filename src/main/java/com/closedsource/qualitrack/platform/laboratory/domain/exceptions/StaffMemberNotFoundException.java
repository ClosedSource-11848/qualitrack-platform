package com.closedsource.qualitrack.platform.laboratory.domain.exceptions;

/**
 * Exception thrown when a staff member is not found.
 * @summary
 * This exception is thrown when a staff member is not found in the database.
 * @see RuntimeException
 */
public class StaffMemberNotFoundException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param staffMemberId The ID of the staff member that was not found.
     */
    public StaffMemberNotFoundException(String staffMemberId) {
        super(String.format("Staff member with ID %s not found.", staffMemberId));
    }
}