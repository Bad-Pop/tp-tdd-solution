package fr.esgi.cleancode.database;

import fr.esgi.cleancode.model.DrivingLicence;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryDatabase {

    private static InMemoryDatabase INSTANCE;
    private static final java.util.Map<UUID, DrivingLicence> IN_MEMORY_DATABASE = new ConcurrentHashMap<>();

    private InMemoryDatabase() {
    }

    public static synchronized InMemoryDatabase getInstance() {
        if (INSTANCE == null) {
            return new InMemoryDatabase();
        }
        return INSTANCE;
    }

    public DrivingLicence save(UUID drivingLicenceId, DrivingLicence drivingLicenceToSave) {
        IN_MEMORY_DATABASE.put(drivingLicenceId, drivingLicenceToSave);
        return drivingLicenceToSave;
    }

    public Optional<DrivingLicence> findById(UUID id) {
        return Optional.ofNullable(IN_MEMORY_DATABASE.get(id));
    }
}
