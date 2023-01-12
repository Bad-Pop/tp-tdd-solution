package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import fr.esgi.cleancode.validation.DriverSocialSecurityNumberValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceSaverServiceTest {

    @InjectMocks
    private DrivingLicenceSaverService service;

    @Mock
    private DriverSocialSecurityNumberValidator driverSocialSecurityNumberValidator;

    @Mock
    private DrivingLicenceIdGenerationService idGenerationService;

    @Mock
    private InMemoryDatabase database;

    @Captor
    private ArgumentCaptor<DrivingLicence> licenceCaptor;

    @Test
    void should_save_new_licence() {
        final var driverSSNumber = "driverSSNumber";
        final var id = UUID.randomUUID();

        final var expectedLicence = DrivingLicence.builder()
                .id(id)
                .driverSocialSecurityNumber(driverSSNumber)
                .availablePoints(12)
                .build();

        when(driverSocialSecurityNumberValidator.isValid(driverSSNumber)).thenReturn(true);
        when(idGenerationService.generateNewDrivingLicenceId()).thenReturn(id);

        when(database.save(eq(id), any(DrivingLicence.class)))
                .thenAnswer(invocation -> invocation.getArgument(1, DrivingLicence.class));

        final var actual = service.create(driverSSNumber);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expectedLicence);

        verify(driverSocialSecurityNumberValidator).isValid(driverSSNumber);
        verify(idGenerationService).generateNewDrivingLicenceId();
        verify(database).save(eq(id), licenceCaptor.capture());

        assertThat(licenceCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedLicence);

        verifyNoMoreInteractions(driverSocialSecurityNumberValidator, idGenerationService, database);
    }

    @Test
    void should_not_save_if_driverSSNumber_is_invalid() {
        final var driverSSNumber = "driverSSNumber";

        when(driverSocialSecurityNumberValidator.isValid(driverSSNumber)).thenReturn(false);

        assertThatExceptionOfType(InvalidDriverSocialSecurityNumberException.class)
                .isThrownBy(() -> service.create(driverSSNumber));

        verify(driverSocialSecurityNumberValidator).isValid(driverSSNumber);
        verifyNoMoreInteractions(driverSocialSecurityNumberValidator);
        verifyNoInteractions(idGenerationService, database);
    }
}