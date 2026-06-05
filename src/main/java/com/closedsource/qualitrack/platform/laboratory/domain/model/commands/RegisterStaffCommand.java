package com.closedsource.qualitrack.platform.laboratory.domain.model.commands;

public record RegisterStaffCommand(
        String laboratoryId,
        String firstName,
        String lastName,
        String role
) {}