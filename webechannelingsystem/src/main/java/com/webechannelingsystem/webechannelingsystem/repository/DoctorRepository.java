package com.webechannelingsystem.webechannelingsystem.repository;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByStatus(String status);

    // Count by status
    long countByStatus(String status);

    @Query("SELECT d.email FROM Doctor d")
    List<String> findAllDoctorEmails();
}


