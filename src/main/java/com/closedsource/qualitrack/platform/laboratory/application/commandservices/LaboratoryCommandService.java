package com.closedsource.qualitrack.platform.laboratory.application.commandservices;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.Laboratory;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateLaboratoryCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.UpdateLaboratoryCommand;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;

/**
 * Service interface for handling laboratory-related commands.
 */
public interface LaboratoryCommandService {

    // Cambiado a Long para reflejar el ID nativo autogenerado
    Result<Long, ApplicationError> handle(CreateLaboratoryCommand command);

    Result<Laboratory, ApplicationError> handle(UpdateLaboratoryCommand command);
}