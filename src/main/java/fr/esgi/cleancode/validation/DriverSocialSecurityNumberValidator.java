package fr.esgi.cleancode.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DriverSocialSecurityNumberValidator {
    public boolean isValid(String driverSocialSecurityNumber) {
        return driverSocialSecurityNumber != null
                && driverSocialSecurityNumber.length() == 15
                && driverSocialSecurityNumber.matches("\\d+");
    }
}
