package com.closedsource.qualitrack.platform.batch.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.batch.domain.model.commands.CreateBatchCommand;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.resources.CreateBatchResource;

/**
 * Assembler to convert a CreateBatchResource to a CreateBatchCommand.
 */
public class CreateBatchCommandFromResourceAssembler {

    /**
     * Converts a CreateBatchResource to a CreateBatchCommand.
     *
     * @param resource The {@link CreateBatchResource} resource to convert.
     * @return The {@link CreateBatchCommand} command that results from the conversion.
     */
    public static CreateBatchCommand toCommandFromResource(CreateBatchResource resource) {
        return new CreateBatchCommand(
                resource.labId(),
                resource.productId(),
                resource.batchNumber(),
                resource.quantity(),
                resource.startDate(),
                resource.notes()
        );
    }
}