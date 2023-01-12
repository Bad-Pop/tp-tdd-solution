package fr.esgi.cleancode.service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DrivingLicenceIdGenerationServiceTest {
    private final DrivingLicenceIdGenerationService service = new DrivingLicenceIdGenerationService();

    @Test
    void should_generate_valid_UUID() {
        final var actual = service.generateNewDrivingLicenceId();
        assertThat(actual)
                .isNotNull()
                .isEqualTo(UUID.fromString(actual.toString()));
    }
}