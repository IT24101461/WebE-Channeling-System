package com.webechannelingsystem.web_echannelingsystem.repository;

import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Get doctors by status
    List<Doctor> findByStatus(String status);

    // Count doctors by status
    long countByStatus(String status);
}


