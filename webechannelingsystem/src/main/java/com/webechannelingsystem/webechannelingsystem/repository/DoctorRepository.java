package com.webechannelingsystem.webechannelingsystem.repository;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Get doctors by status
    List<Doctor> findByStatus(String status);

    // Count doctors by status
    long countByStatus(String status);

    @Query("SELECT d.email FROM Doctor d where d.status = 'APPROVED'")
    List<String> findAllDoctorEmails();
}







