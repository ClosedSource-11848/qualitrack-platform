package com.closedsource.qualitrack.platform.ca.interfaces.rest;

import com.closedsource.qualitrack.platform.ca.application.queryservices.CaQueryService;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetComplianceEventsByRelatedEntityIdQuery;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.ComplianceEventResource;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.transform.ComplianceEventResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes compliance audit event resources.
 */
@RestController
@RequestMapping(value = "/api/v1/compliance-events", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Compliance Events", description = "Compliance audit event endpoints")
public class ComplianceEventController {

    private final CaQueryService caQueryService;

    public ComplianceEventController(CaQueryService caQueryService) {
        this.caQueryService = caQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ComplianceEventResource>> getComplianceEvents(
            @RequestParam Long relatedEntityId
    ) {
        var events = caQueryService.handle(
                new GetComplianceEventsByRelatedEntityIdQuery(relatedEntityId)
        );

        var resources = events.stream()
                .map(ComplianceEventResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}