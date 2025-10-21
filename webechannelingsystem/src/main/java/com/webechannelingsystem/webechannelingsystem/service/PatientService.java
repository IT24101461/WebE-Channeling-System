package com.webechannelingsystem.webechannelingsystem.service;

import com.webechannelingsystem.webechannelingsystem.model.Appointment;
import com.webechannelingsystem.webechannelingsystem.model.Patient;
import com.webechannelingsystem.webechannelingsystem.repository.AppointmentRepository;
import com.webechannelingsystem.webechannelingsystem.repository.PatientRepository;
import com.webechannelingsystem.webechannelingsystem.model.Patient;
import com.webechannelingsystem.webechannelingsystem.repository.AppointmentRepository;
import com.webechannelingsystem.webechannelingsystem.repository.PatientRepository;
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

    public void registerPatient(Patient patient) {
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
}