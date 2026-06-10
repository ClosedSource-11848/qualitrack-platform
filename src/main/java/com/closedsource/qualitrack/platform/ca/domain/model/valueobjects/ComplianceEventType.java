package com.closedsource.qualitrack.platform.ca.domain.model.valueobjects;

/**
 * Represents the type of compliance event recorded in the audit trail.
 */
public enum ComplianceEventType {
    DEVIATION_ALERT_CREATED,
    DEVIATION_ALERT_ACKNOWLEDGED,
    DEVIATION_ALERT_RESOLVED,
    NOTIFICATION_PREFERENCE_UPDATED,
    BATCH_BLOCKED,
    BATCH_RELEASED,
    BATCH_REJECTED,
    RAW_MATERIAL_LOW_STOCK,
    EQUIPMENT_CALIBRATION_EXPIRED,
    EQUIPMENT_DEVIATION_DETECTED
}