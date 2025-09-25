package com.webechannelingsystem.web_echannelingsystem.repository;

import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
}

