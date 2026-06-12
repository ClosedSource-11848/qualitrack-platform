package com.closedsource.qualitrack.platform.ra.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.ra.domain.model.commands.ExportEquipmentLogCommand;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.ExportEquipmentLogResource;

/**
 * Assembler that transforms equipment log REST resources into application commands.
 */
public final class ExportEquipmentLogCommandFromResourceAssembler {
    private ExportEquipmentLogCommandFromResourceAssembler() {
    }

    /**
     * Converts an equipment log export resource into a command.
     *
     * @param resource the equipment log export request resource
     * @return the equipment log export command
     */
    public static ExportEquipmentLogCommand toCommandFromResource(
            ExportEquipmentLogResource resource
    ) {
        return new ExportEquipmentLogCommand(
                resource.equipmentId(),
                resource.startDate(),
                resource.endDate(),
                resource.format(),
                resource.requestedBy()
        );
    }
}