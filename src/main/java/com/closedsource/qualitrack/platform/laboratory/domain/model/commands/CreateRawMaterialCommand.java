package com.closedsource.qualitrack.platform.laboratory.domain.model.commands;

public record CreateRawMaterialCommand(
        String laboratoryId,
        String name,
        String unit,
        Integer initialStock
) {}