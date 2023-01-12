package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.validation.DriverSocialSecurityNumberValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DrivingLicenceSaverService {

    private final DriverSocialSecurityNumberValidator driverSocialSecurityNumberValidator;
    private final DrivingLicenceIdGenerationService idGenerationService;
    private final InMemoryDatabase database;

    public DrivingLicence create(String driverSocialSecurityNumber) {
        if (driverSocialSecurityNumberValidator.isValid(driverSocialSecurityNumber)) {
            final var drivingLicenceId = idGenerationService.generateNewDrivingLicenceId();
            final var drivingLicence = DrivingLicence.builder()
                    .id(drivingLicenceId)
                    .driverSocialSecurityNumber(driverSocialSecurityNumber)
                    .build();
            return database.save(drivingLicenceId, drivingLicence);
        }
        throw new InvalidDriverSocialSecurityNumberException("The given ss number is invalid");
    }
}
