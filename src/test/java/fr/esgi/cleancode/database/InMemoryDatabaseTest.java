package fr.esgi.cleancode.database;

import fr.esgi.cleancode.model.DrivingLicence;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDatabaseTest {

    private final InMemoryDatabase db = InMemoryDatabase.getInstance();

    @Test
    void should_save_and_find_driving_licence() {
        final var id = UUID.randomUUID();
        final var given = DrivingLicence.builder().id(id).build();

        Assertions.assertThatNoException().isThrownBy(() -> db.save(id, given));
        final var actual = db.findById(id);

        assertThat(actual).containsSame(given);
    }
}