package fr.esgi.cleancode.validation;

import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DriverSocialSecurityNumberValidatorTest {

    private final DriverSocialSecurityNumberValidator validator = new DriverSocialSecurityNumberValidator();

    @ParameterizedTest
    @ValueSource(strings = {"123456789012345", "098765432109876"})
    void should_validate(String validSSNumber) {
        final var actual = validator.isValid(validSSNumber);
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"lorem ipsum", "azertyuiopmlkjh", "09876543210987654321", "098654"})
    void should_not_validate(String invalidSSNumber) {
        val actual = validator.isValid(invalidSSNumber);
        assertThat(actual).isFalse();
    }
}