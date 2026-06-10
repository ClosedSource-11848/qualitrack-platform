package com.closedsource.qualitrack.platform.ca.interfaces.rest.resources;

import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.AlertSeverity;

public record NotificationPreferenceResource(
        Long id,
        Long userId,
        Boolean emailEnabled,
        Boolean smsEnabled,
        Boolean inAppEnabled,
        AlertSeverity minimumSeverity
) {
}