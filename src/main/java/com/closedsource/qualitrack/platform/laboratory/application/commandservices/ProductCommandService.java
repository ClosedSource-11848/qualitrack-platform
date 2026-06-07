package com.closedsource.qualitrack.platform.laboratory.application.commandservices;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateProductCommand;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;

/**
 * Application service contract for commands over pharmaceutical products.
 */
public interface ProductCommandService {

    /**
     * Handles the creation of a new pharmaceutical product in a laboratory's catalog.
     *
     * @param command command containing initial product data
     * @return created product identifier (domain ID) or an application error
     * @see CreateProductCommand
     */
    Result<Long, ApplicationError> handle(CreateProductCommand command);
}