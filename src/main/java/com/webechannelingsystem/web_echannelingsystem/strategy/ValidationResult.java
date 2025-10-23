package com.webechannelingsystem.web_echannelingsystem.strategy;

import java.util.List;

/**
 * Encapsulates the result of password validation
 * Contains validation status and any error messages
 */
public class ValidationResult {

    private boolean valid;
    private List<String> errors;

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors;
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return String.join(", ", errors);
    }
}