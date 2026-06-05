package com.closedsource.qualitrack.platform.laboratory.application.commandservices;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.Laboratory;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateLaboratoryCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.UpdateLaboratoryCommand;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;

/**
 * Application service contract for commands over the {@link Laboratory} aggregate.
 */
public interface LaboratoryCommandService {

    /**
     * Handles laboratory creation.
     *
     * @param command command containing initial laboratory data
     * @return created laboratory identifier (domain ID) or an application error
     * @see CreateLaboratoryCommand
     */
    Result<String, ApplicationError> handle(CreateLaboratoryCommand command);

    /**
     * Handles laboratory profile update.
     *
     * @param command command containing target laboratory id and new data
     * @return updated laboratory aggregate or an application error
     * @see UpdateLaboratoryCommand
     */
    Result<Laboratory, ApplicationError> handle(UpdateLaboratoryCommand command);
}