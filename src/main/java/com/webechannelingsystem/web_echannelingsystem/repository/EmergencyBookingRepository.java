package com.webechannelingsystem.web_echannelingsystem.repository;

import com.webechannelingsystem.web_echannelingsystem.model.EmergencyBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmergencyBookingRepository extends JpaRepository<EmergencyBooking, Long> {

    // Find emergency bookings by patient ID
    List<EmergencyBooking> findByPatientId(Long patientId);

    // Find emergency bookings by patient ID and status
    List<EmergencyBooking> findByPatientIdAndStatus(Long patientId, String status);

    // Find emergency bookings by doctor ID
    List<EmergencyBooking> findByDoctorId(Integer doctorId);

    // Find pending emergency bookings
    List<EmergencyBooking> findByStatus(String status);

    // Find emergency bookings by patient email
    List<EmergencyBooking> findByPatientEmail(String email);

    // Find emergency bookings within a time range
    @Query("SELECT eb FROM EmergencyBooking eb WHERE eb.requestedTime BETWEEN :startTime AND :endTime")
    List<EmergencyBooking> findByRequestedTimeBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Find upcoming emergency bookings for a patient
    @Query("SELECT eb FROM EmergencyBooking eb WHERE eb.patient.id = :patientId " +
            "AND eb.requestedTime > :currentTime AND eb.status != 'CANCELLED' " +
            "ORDER BY eb.requestedTime ASC")
    List<EmergencyBooking> findUpcomingByPatientId(
            @Param("patientId") Long patientId,
            @Param("currentTime") LocalDateTime currentTime
    );

    // Find past emergency bookings for a patient
    @Query("SELECT eb FROM EmergencyBooking eb WHERE eb.patient.id = :patientId " +
            "AND (eb.requestedTime < :currentTime OR eb.status = 'COMPLETED') " +
            "ORDER BY eb.requestedTime DESC")
    List<EmergencyBooking> findPastByPatientId(
            @Param("patientId") Long patientId,
            @Param("currentTime") LocalDateTime currentTime
    );
}