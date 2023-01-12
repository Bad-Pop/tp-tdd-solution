package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicencePointWithdrawService {

    private final InMemoryDatabase database;

    public DrivingLicence withdrawPointsAndSave(UUID drivingLicenceId, int pointsToWithdraw) {
        return database.findById(drivingLicenceId)
                .map(drivingLicence -> removePoints(drivingLicence, pointsToWithdraw))
                .map(drivingLicence -> database.save(drivingLicenceId, drivingLicence))
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find driving licence"));
    }

    private DrivingLicence removePoints(DrivingLicence drivingLicence, int pointsToRemove) {
        final var pointsAfterRemoval = Math.max(drivingLicence.getAvailablePoints() - pointsToRemove, 0);
        return drivingLicence.withAvailablePoints(pointsAfterRemoval);
    }
}
