package com.webechannelingsystem.webechannelingsystem.repository;

import com.webechannelingsystem.webechannelingsystem.model.Appointment;
import com.webechannelingsystem.webechannelingsystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdAndStatus(Long patientId, String status);
    List<Appointment> findByPatientIdAndAppointmentTimeAfterAndStatus(Long patientId, LocalDateTime dateTime, String status);
    List<Appointment> findByPatientIdAndAppointmentTimeBeforeOrStatus(Long patientId, LocalDateTime dateTime, String status);
    void deleteByPatientId(Long patientId); // Added derived delete method
}