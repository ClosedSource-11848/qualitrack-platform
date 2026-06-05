package com.closedsource.qualitrack.platform.laboratory.application.internal.commandservices;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.StaffCommandService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.StaffMember;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.DeactivateStaffCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.RegisterStaffCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.LaboratoryRepository;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.StaffRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service implementation that executes staff commands.
 */
@Service
public class StaffCommandServiceImpl implements StaffCommandService {
    private final StaffRepository staffRepository;
    private final LaboratoryRepository laboratoryRepository;

    public StaffCommandServiceImpl(StaffRepository staffRepository, LaboratoryRepository laboratoryRepository) {
        this.staffRepository = staffRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    @Override
    public Result<String, ApplicationError> handle(RegisterStaffCommand command) {
        if (!laboratoryRepository.existsById(command.laboratoryId())) {
            return Result.failure(ApplicationError.notFound("Laboratory", command.laboratoryId()));
        }

        try {
            var staffMember = new StaffMember(command);
            var savedStaff = staffRepository.save(staffMember);
            return Result.success(savedStaff.getId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("StaffMember", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("register-staff", e.getMessage()));
        }
    }

    @Override
    public Result<String, ApplicationError> handle(DeactivateStaffCommand command) {
        return staffRepository.findById(command.staffMemberId()).map(staff -> {
            if (!staff.getLaboratoryId().equals(command.laboratoryId())) {
                return Result.<String, ApplicationError>failure(ApplicationError.businessRuleViolation(
                        "Staff-Laboratory Mismatch",
                        "Staff member does not belong to the specified laboratory"
                ));
            }
            if (!staff.isActive()) {
                return Result.<String, ApplicationError>failure(ApplicationError.conflict(
                        "StaffMember",
                        "Staff member is already inactive"
                ));
            }

            staff.deactivate();
            var savedStaff = staffRepository.save(staff);
            return Result.<String, ApplicationError>success(savedStaff.getId());

        }).orElseGet(() -> Result.failure(ApplicationError.notFound("StaffMember", command.staffMemberId())));
    }
}