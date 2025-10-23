package com.webechannelingsystem.web_echannelingsystem.strategy;

import org.springframework.stereotype.Component;

/**
 * Strategy Pattern - Concrete Strategy
 * Validates that password meets minimum length requirement
 */
@Component
public class MinimumLengthStrategy implements PasswordValidationStrategy {

    private static final int MIN_LENGTH = 8;

    @Override
    public boolean validate(String password) {
        return password != null && password.length() >= MIN_LENGTH;
    }

    @Override
    public String getErrorMessage() {
        return "Password must be at least " + MIN_LENGTH + " characters long";
    }
}