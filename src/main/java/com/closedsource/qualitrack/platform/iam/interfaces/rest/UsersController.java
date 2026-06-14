package com.closedsource.qualitrack.platform.iam.interfaces.rest;

import com.closedsource.qualitrack.platform.iam.application.commandservices.UserCommandService;
import com.closedsource.qualitrack.platform.iam.application.queryservices.UserQueryService;
import com.closedsource.qualitrack.platform.iam.domain.model.commands.AssignRoleCommand;
import com.closedsource.qualitrack.platform.iam.domain.model.commands.DeactivateUserCommand;
import com.closedsource.qualitrack.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.closedsource.qualitrack.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.closedsource.qualitrack.platform.iam.interfaces.rest.resources.UserResource;
import com.closedsource.qualitrack.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for IAM user administration operations.
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "IAM user management endpoints")
public class UsersController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UsersController(
            UserQueryService userQueryService,
            UserCommandService userCommandService
    ) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping
    @Operation(summary = "Get users", description = "Retrieves all IAM users.")
    public ResponseEntity<List<UserResource>> getUsers() {
        var users = userQueryService.handle(new GetAllUsersQuery());

        var resources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieves an IAM user by identifier.")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var user = userQueryService.handle(new GetUserByIdQuery(userId));

        if (user.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @PatchMapping("/{userId}/roles/{roleName}")
    @Operation(summary = "Assign role", description = "Assigns a role to an IAM user.")
    public ResponseEntity<?> assignRole(
            @PathVariable Long userId,
            @PathVariable String roleName
    ) {
        var result = userCommandService.handle(new AssignRoleCommand(userId, roleName));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                id -> id,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{userId}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivates an IAM user account.")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        var result = userCommandService.handle(new DeactivateUserCommand(userId));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                id -> id,
                HttpStatus.OK
        );
    }
}