package com.webechannelingsystem.web_echannelingsystem.strategy;

import org.springframework.stereotype.Component;

/**
 * Strategy Pattern - Concrete Strategy
 * Validates that password contains both letters and numbers
 */
@Component
public class AlphanumericStrategy implements PasswordValidationStrategy {

    @Override
    public boolean validate(String password) {
        if (password == null) {
            return false;
        }

        boolean hasLetter = password.matches(".*[A-Za-z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        return hasLetter && hasDigit;
    }

    @Override
    public String getErrorMessage() {
        return "Password must contain both letters and numbers";
    }
}