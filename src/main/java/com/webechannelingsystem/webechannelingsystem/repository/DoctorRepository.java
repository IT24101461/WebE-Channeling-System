package com.webechannelingsystem.webechannelingsystem.repository;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByStatus(String status);

    // Count by status
    long countByStatus(String status);
}


