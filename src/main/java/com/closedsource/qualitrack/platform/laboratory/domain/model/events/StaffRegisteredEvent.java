package com.closedsource.qualitrack.platform.laboratory.domain.model.events;

/**
 * Domain event published when a new staff member is registered to a laboratory.
 *
 * <p>Can be listened to by the IAM context to automatically provision credentials
 * or send welcome emails.</p>
 *
 * @param staffMemberId The identity of the newly registered staff member.
 * @param laboratoryId  The identity of the laboratory they belong to.
 * @param fullName      The full name of the staff member.
 * @param role          The assigned operational role.
 */
public record StaffRegisteredEvent(
        String staffMemberId,
        String laboratoryId,
        String fullName,
        String role) {
}