package com.webechannelingsystem.web_echannelingsystem.singleton;

/**
 * Singleton Pattern Implementation
 * Manages global configuration settings for the patient management system.
 * Ensures only ONE instance exists throughout the application lifecycle.
 */
public class PatientSystemConfiguration {

    // The single instance (Eager Initialization)
    private static PatientSystemConfiguration instance = new PatientSystemConfiguration();

    // Configuration properties
    private int maxLoginAttempts;
    private int sessionTimeoutMinutes;
    private boolean emailVerificationRequired;
    private int minPasswordLength;
    private String systemName;
    private boolean maintenanceMode;

    /**
     * Private constructor prevents instantiation from outside
     * This is KEY to the Singleton pattern
     */
    private PatientSystemConfiguration() {
        // Initialize default values
        this.maxLoginAttempts = 3;
        this.sessionTimeoutMinutes = 30;
        this.emailVerificationRequired = false;
        this.minPasswordLength = 8;
        this.systemName = "E-Channeling Patient Portal";
        this.maintenanceMode = false;
    }

    /**
     * Global access point to get the single instance
     * @return the singleton instance
     */
    public static PatientSystemConfiguration getInstance() {
        return instance;
    }

    // Getters and Setters for configuration properties

    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public int getSessionTimeoutMinutes() {
        return sessionTimeoutMinutes;
    }

    public void setSessionTimeoutMinutes(int sessionTimeoutMinutes) {
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
    }

    public boolean isEmailVerificationRequired() {
        return emailVerificationRequired;
    }

    public void setEmailVerificationRequired(boolean emailVerificationRequired) {
        this.emailVerificationRequired = emailVerificationRequired;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    /**
     * Displays current configuration (useful for debugging/demo)
     */
    public void displayConfiguration() {
        System.out.println("=== Patient System Configuration ===");
        System.out.println("System Name: " + systemName);
        System.out.println("Max Login Attempts: " + maxLoginAttempts);
        System.out.println("Session Timeout: " + sessionTimeoutMinutes + " minutes");
        System.out.println("Email Verification Required: " + emailVerificationRequired);
        System.out.println("Min Password Length: " + minPasswordLength);
        System.out.println("Maintenance Mode: " + maintenanceMode);
        System.out.println("===================================");
    }
}
