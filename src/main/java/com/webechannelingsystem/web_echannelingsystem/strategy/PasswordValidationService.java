package com.webechannelingsystem.web_echannelingsystem.strategy;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Strategy Pattern - Context Class
 * This service manages and applies multiple password validation strategies.
 * It demonstrates the Strategy Pattern by allowing dynamic selection and
 * execution of different validation algorithms.
 */
@Service
public class PasswordValidationService {

    private List<PasswordValidationStrategy> strategies;

    /**
     * Initialize the validation strategies on service startup
     * This is where you can add or remove strategies as needed
     */
    @PostConstruct
    public void init() {
        strategies = new ArrayList<>();
        strategies.add(new MinimumLengthStrategy());
        strategies.add(new AlphanumericStrategy());
    }

    /**
     * Allows manual configuration of strategies (useful for testing)
     */
    public void setStrategies(List<PasswordValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * Adds a new validation strategy to the existing list
     */
    public void addStrategy(PasswordValidationStrategy strategy) {
        if (this.strategies == null) {
            this.strategies = new ArrayList<>();
        }
        this.strategies.add(strategy);
    }

    /**
     * Validates password against all configured strategies
     * @param password the password to validate
     * @return ValidationResult containing status and any error messages
     */
    public ValidationResult validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        // Apply each strategy
        for (PasswordValidationStrategy strategy : strategies) {
            if (!strategy.validate(password)) {
                errors.add(strategy.getErrorMessage());
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }
}