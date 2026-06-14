package com.closedsource.qualitrack.platform.iam.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.iam.domain.model.commands.SignUpCommand;
import com.closedsource.qualitrack.platform.iam.interfaces.rest.resources.SignUpResource;

/**
 * Assembler that maps sign-up REST resources to domain commands.
 */
public class SignUpCommandFromResourceAssembler {

    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(
                resource.username(),
                resource.password(),
                resource.roles(),
                resource.laboratoryId()
        );
    }
}