package com.webechannelingsystem.web_echannelingsystem.service;

import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.repository.AppointmentRepository;
import com.webechannelingsystem.web_echannelingsystem.repository.PatientRepository;
import com.webechannelingsystem.web_echannelingsystem.strategy.PasswordValidationService;
import com.webechannelingsystem.web_echannelingsystem.strategy.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PasswordValidationService passwordValidationService;

    /**
     * Registers a new patient with password validation using Strategy Pattern
     */
    public void registerPatient(Patient patient) {
        // Apply Strategy Pattern for password validation
        ValidationResult result = passwordValidationService.validatePassword(patient.getPassword());

        if (!result.isValid()) {
            throw new IllegalArgumentException(
                    "Password validation failed: " + result.getErrorMessage()
            );
        }

        patientRepository.save(patient);
    }

    public boolean validatePatientCredentials(String email, String password) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        return patientOpt.isPresent() && patientOpt.get().getPassword().equals(password);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Transactional
    public void deletePatient(String email) {
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        Patient patient = patientOpt.get();
        appointmentRepository.deleteByPatientId(patient.getId());
        patientRepository.delete(patient);
    }

    /**
     * Updates patient profile with password validation if password is being changed
     */
    public void updatePatient(Patient patient) {
        // If password is being updated (not null and not empty), validate it
        if (patient.getPassword() != null && !patient.getPassword().isEmpty()) {
            ValidationResult result = passwordValidationService.validatePassword(patient.getPassword());

            if (!result.isValid()) {
                throw new IllegalArgumentException(
                        "Password validation failed: " + result.getErrorMessage()
                );
            }
        }

        patientRepository.save(patient);
    }
}