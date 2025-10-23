package com.webechannelingsystem.web_echannelingsystem.service;

import com.webechannelingsystem.web_echannelingsystem.singleton.PatientSystemConfiguration;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service that tracks login attempts per user
 * Uses Singleton Pattern to get max login attempts configuration
 */
@Service
public class LoginAttemptTracker {

    // Tracks failed login attempts per email
    private Map<String, Integer> attemptCache = new HashMap<>();

    // Tracks when the account was locked
    private Map<String, LocalDateTime> lockTimeCache = new HashMap<>();

    // Lock duration in minutes
    private static final int LOCK_DURATION_MINUTES = 1;

    /**
     * Records a failed login attempt
     * @param email the user's email
     */
    public void recordFailedAttempt(String email) {
        int attempts = attemptCache.getOrDefault(email, 0);
        attempts++;
        attemptCache.put(email, attempts);

        // If max attempts reached, record lock time
        int maxAttempts = PatientSystemConfiguration.getInstance().getMaxLoginAttempts();
        if (attempts >= maxAttempts) {
            lockTimeCache.put(email, LocalDateTime.now());
        }
    }

    /**
     * Clears login attempts after successful login
     * @param email the user's email
     */
    public void resetAttempts(String email) {
        attemptCache.remove(email);
        lockTimeCache.remove(email);
    }

    /**
     * Checks if user has exceeded max login attempts
     * Uses Singleton to get the max attempts configuration
     * Also checks if lock duration has expired
     * @param email the user's email
     * @return true if user is blocked, false otherwise
     */
    public boolean isBlocked(String email) {
        // Get max attempts from Singleton Configuration
        int maxAttempts = PatientSystemConfiguration.getInstance().getMaxLoginAttempts();

        int attempts = attemptCache.getOrDefault(email, 0);

        // If attempts >= max, check if lock has expired
        if (attempts >= maxAttempts) {
            LocalDateTime lockTime = lockTimeCache.get(email);
            if (lockTime != null) {
                LocalDateTime unlockTime = lockTime.plusMinutes(LOCK_DURATION_MINUTES);

                // If lock has expired, reset attempts
                if (LocalDateTime.now().isAfter(unlockTime)) {
                    resetAttempts(email);
                    return false;
                }
                return true; // Still locked
            }
        }

        return false; // Not blocked
    }

    /**
     * Gets the number of remaining attempts
     * @param email the user's email
     * @return number of attempts remaining
     */
    public int getRemainingAttempts(String email) {
        int maxAttempts = PatientSystemConfiguration.getInstance().getMaxLoginAttempts();
        int currentAttempts = attemptCache.getOrDefault(email, 0);
        return Math.max(0, maxAttempts - currentAttempts);
    }

    /**
     * Gets the time remaining until account is unlocked
     * @param email the user's email
     * @return minutes remaining until unlock, or 0 if not locked
     */
    public long getMinutesUntilUnlock(String email) {
        LocalDateTime lockTime = lockTimeCache.get(email);
        if (lockTime == null) {
            return 0;
        }

        LocalDateTime unlockTime = lockTime.plusMinutes(LOCK_DURATION_MINUTES);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(unlockTime)) {
            return 0;
        }

        return java.time.Duration.between(now, unlockTime).toMinutes();
    }
}
