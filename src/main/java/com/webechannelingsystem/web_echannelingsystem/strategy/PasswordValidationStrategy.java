package com.webechannelingsystem.web_echannelingsystem.strategy;

/**
 * Strategy Pattern - Strategy Interface
 * This interface defines the contract for all password validation strategies.
 * Each concrete strategy implements different validation rules.
 */
public interface PasswordValidationStrategy {

    /**
     * Validates the given password according to the strategy's rule
     * @param password the password to validate
     * @return true if password meets the criteria, false otherwise
     */
    boolean validate(String password);

    /**
     * Returns a descriptive error message if validation fails
     * @return error message string
     */
    String getErrorMessage();
}