package com.closedsource.qualitrack.platform.laboratory.domain.model.commands;

public record UpdateLaboratoryCommand(
        String laboratoryId,
        String name,
        String regulationCode
) {}