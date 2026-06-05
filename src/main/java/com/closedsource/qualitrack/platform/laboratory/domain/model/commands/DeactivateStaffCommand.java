package com.closedsource.qualitrack.platform.laboratory.domain.model.commands;

public record DeactivateStaffCommand(
        String laboratoryId,
        String staffMemberId
) {}