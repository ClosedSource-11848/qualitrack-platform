package com.closedsource.qualitrack.platform.laboratory.application.internal.commandservices;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.ProductCommandService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.PharmaceuticalProduct;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateProductCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.LaboratoryRepository;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.ProductRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service implementation that executes pharmaceutical product commands.
 */
@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private final ProductRepository productRepository;
    private final LaboratoryRepository laboratoryRepository;

    public ProductCommandServiceImpl(ProductRepository productRepository, LaboratoryRepository laboratoryRepository) {
        this.productRepository = productRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    @Override
    public Result<String, ApplicationError> handle(CreateProductCommand command) {
        if (!laboratoryRepository.existsById(command.laboratoryId())) {
            return Result.failure(ApplicationError.notFound("Laboratory", command.laboratoryId()));
        }

        if (productRepository.findByNameAndLaboratoryId(command.name(), command.laboratoryId()).isPresent()) {
            return Result.failure(ApplicationError.conflict(
                    "PharmaceuticalProduct",
                    "Product with name '%s' already exists in this laboratory".formatted(command.name())
            ));
        }

        try {
            var product = new PharmaceuticalProduct(command);
            var savedProduct = productRepository.save(product);
            return Result.success(savedProduct.getId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("PharmaceuticalProduct", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("create-product", e.getMessage()));
        }
    }
}