package com.closedsource.qualitrack.platform.ca.interfaces.rest;

import com.closedsource.qualitrack.platform.ca.application.commandservices.CaCommandService;
import com.closedsource.qualitrack.platform.ca.application.queryservices.CaQueryService;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetNotificationPreferenceByUserIdQuery;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.NotificationPreferenceResource;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.UpdateNotificationPreferenceResource;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.transform.NotificationPreferenceResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.transform.UpdateNotificationPreferenceCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes user notification preference resources.
 */
@RestController
@RequestMapping(value = "/api/v1/notification-preferences", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notification Preferences", description = "Notification preference management endpoints")
public class NotificationPreferenceController {

    private final CaCommandService caCommandService;
    private final CaQueryService caQueryService;

    public NotificationPreferenceController(CaCommandService caCommandService, CaQueryService caQueryService) {
        this.caCommandService = caCommandService;
        this.caQueryService = caQueryService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<NotificationPreferenceResource> getPreferencesByUserId(@PathVariable Long userId) {
        var preference = caQueryService.handle(new GetNotificationPreferenceByUserIdQuery(userId));

        if (preference.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                NotificationPreferenceResourceFromEntityAssembler.toResourceFromEntity(preference.get())
        );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updatePreferences(
            @PathVariable Long userId,
            @RequestBody UpdateNotificationPreferenceResource resource
    ) {
        var command = UpdateNotificationPreferenceCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var result = caCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                NotificationPreferenceResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}