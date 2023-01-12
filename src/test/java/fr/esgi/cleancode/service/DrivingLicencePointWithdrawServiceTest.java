package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DrivingLicencePointWithdrawServiceTest {

    @InjectMocks
    private DrivingLicencePointWithdrawService service;

    @Mock
    private InMemoryDatabase database;

    @Captor
    private ArgumentCaptor<DrivingLicence> drivingLicenceCaptor;

    @ParameterizedTest
    @CsvSource({"1,12,11", "6,1,0"})
    void should_withdraw_points(int pointsToWithdraw, int currentLicencePoints, int expectedPoints) {
        final var id = UUID.randomUUID();
        final var licence = DrivingLicence.builder().id(id).availablePoints(currentLicencePoints).build();

        when(database.findById(id)).thenReturn(Optional.of(licence));
        when(database.save(eq(id), any(DrivingLicence.class))).thenReturn(licence);

        final var actual = service.withdrawPointsAndSave(id, pointsToWithdraw);
        assertThat(actual).isEqualTo(licence);
        verify(database).save(eq(id), drivingLicenceCaptor.capture());
        assertThat(drivingLicenceCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(licence.withAvailablePoints(expectedPoints));
        verifyNoMoreInteractions(database);
    }

    @Test
    void should_throw_ResourceNotFoundException_if_no_driving_licence_was_found() {
        final var id = UUID.randomUUID();

        when(database.findById(id)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> service.withdrawPointsAndSave(id, 1));

        verify(database).findById(id);
        verifyNoMoreInteractions(database);
    }
}