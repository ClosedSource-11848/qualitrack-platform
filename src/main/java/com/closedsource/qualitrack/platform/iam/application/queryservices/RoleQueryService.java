package com.closedsource.qualitrack.platform.iam.application.queryservices;

import com.closedsource.qualitrack.platform.iam.domain.model.entities.Role;
import com.closedsource.qualitrack.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.closedsource.qualitrack.platform.iam.domain.model.queries.GetRoleByNameQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application query service contract for IAM role read operations.
 */
public interface RoleQueryService {

    Optional<Role> handle(GetRoleByNameQuery query);

    List<Role> handle(GetAllRolesQuery query);
}