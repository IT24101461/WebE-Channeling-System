package com.webechannelingsystem.web_echannelingsystem.strategy;

import com.webechannelingsystem.web_echannelingsystem.singleton.PatientSystemConfiguration;
import org.springframework.stereotype.Component;

/**
 * Strategy Pattern - Concrete Strategy
 * Validates that password meets minimum length requirement
 * NOW USES SINGLETON PATTERN to get configuration!
 */
@Component
public class MinimumLengthStrategy implements PasswordValidationStrategy {

    @Override
    public boolean validate(String password) {
        // Get minimum length from Singleton Configuration
        int minLength = PatientSystemConfiguration.getInstance().getMinPasswordLength();

        return password != null && password.length() >= minLength;
    }

    @Override
    public String getErrorMessage() {
        // Get minimum length from Singleton Configuration
        int minLength = PatientSystemConfiguration.getInstance().getMinPasswordLength();

        return "Password must be at least " + minLength + " characters long";
    }
}
