package com.closedsource.qualitrack.platform.laboratory.interfaces.rest;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.ProductCommandService;
import com.closedsource.qualitrack.platform.laboratory.application.queryservices.ProductQueryService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateProductCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.queries.GetProductsByLabIdQuery;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.CreateProductResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.PharmaceuticalProductResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform.ProductResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.resources.MessageResource;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes pharmaceutical products for a specific laboratory.
 */
@RestController
@RequestMapping(value = "/api/v1/laboratories/{laboratoryId}/products", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Laboratories", description = "Laboratory management endpoints") // ¡Mismo tag que LaboratoryController!
public class LaboratoryProductsController {
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    public LaboratoryProductsController(ProductCommandService productCommandService, ProductQueryService productQueryService) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
    }

    @PostMapping
    @Operation(summary = "Add a product to catalog", description = "Creates a new pharmaceutical product in the laboratory's catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Laboratory not found"),
            @ApiResponse(responseCode = "409", description = "Product code or name already exists")
    })
    public ResponseEntity<?> createProduct(
            @PathVariable @Parameter(description = "Laboratory numeric identifier", example = "1", required = true) Long laboratoryId,
            @RequestBody CreateProductResource resource
    ) {
        // Tomamos el laboratoryId de la URL, y el resto de los datos del JSON
        var command = new CreateProductCommand(
                laboratoryId,
                resource.code(),
                resource.name(),
                resource.description(),
                resource.specifications()
        );

        var result = productCommandService.handle(command)
                .map(productId -> new MessageResource("Product created successfully with ID: " + productId));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get laboratory products", description = "Retrieves all pharmaceutical products associated with a specific laboratory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<PharmaceuticalProductResource>> getProductsByLaboratoryId(
            @PathVariable @Parameter(description = "Laboratory numeric identifier", example = "1", required = true) Long laboratoryId
    ) {
        var products = productQueryService.handle(new GetProductsByLabIdQuery(laboratoryId));
        var resources = products.stream().map(ProductResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}