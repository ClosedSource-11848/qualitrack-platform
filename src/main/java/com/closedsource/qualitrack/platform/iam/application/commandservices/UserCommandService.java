package com.closedsource.qualitrack.platform.iam.application.commandservices;

import com.closedsource.qualitrack.platform.iam.domain.model.aggregates.User;
import com.closedsource.qualitrack.platform.iam.domain.model.commands.AssignRoleCommand;
import com.closedsource.qualitrack.platform.iam.domain.model.commands.DeactivateUserCommand;
import com.closedsource.qualitrack.platform.iam.domain.model.commands.SignInCommand;
import com.closedsource.qualitrack.platform.iam.domain.model.commands.SignUpCommand;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;

/**
 * Application command service contract for IAM user operations.
 */
public interface UserCommandService {

    Result<AuthenticatedUser, ApplicationError> handle(SignInCommand command);

    Result<User, ApplicationError> handle(SignUpCommand command);

    Result<Long, ApplicationError> handle(AssignRoleCommand command);

    Result<Long, ApplicationError> handle(DeactivateUserCommand command);

    /**
     * Authenticated user result.
     *
     * @param user authenticated user
     * @param token generated JWT token
     */
    record AuthenticatedUser(
            User user,
            String token
    ) {
    }
}