package com.webechannelingsystem.web_echannelingsystem.service;

import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import com.webechannelingsystem.web_echannelingsystem.model.EmergencyBooking;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.repository.EmergencyBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmergencyBookingService {

    @Autowired
    private EmergencyBookingRepository emergencyBookingRepository;

    @Autowired
    private DoctorService doctorService;

    /**
     * Save a new emergency booking
     */
    public EmergencyBooking saveEmergencyBooking(EmergencyBooking emergencyBooking) {
        return emergencyBookingRepository.save(emergencyBooking);
    }

    /**
     * Create and save emergency booking
     */
    public EmergencyBooking createEmergencyBooking(Patient patient, Doctor doctor,
                                                   LocalDateTime requestedTime,
                                                   String emergencyType) {
        EmergencyBooking booking = new EmergencyBooking();
        booking.setPatient(patient);
        booking.setDoctor(doctor);
        booking.setRequestedTime(requestedTime);
        booking.setPatientEmail(patient.getEmail());
        booking.setEmergencyType(emergencyType);
        booking.setStatus("PENDING");
        booking.setPriority("HIGH");
        booking.setPaymentStatus("PENDING");
        booking.setPaymentMethod("NOT_SELECTED");

        return emergencyBookingRepository.save(booking);
    }

    /**
     * Get all emergency bookings for a patient
     */
    public List<EmergencyBooking> getEmergencyBookingsByPatient(Long patientId) {
        return emergencyBookingRepository.findByPatientId(patientId);
    }

    /**
     * Get upcoming emergency bookings for a patient
     */
    public List<EmergencyBooking> getUpcomingEmergencyBookings(Long patientId) {
        try {
            List<EmergencyBooking> bookings = emergencyBookingRepository.findUpcomingByPatientId(patientId, LocalDateTime.now());
            System.out.println("Found " + bookings.size() + " upcoming emergency bookings for patient ID: " + patientId);
            return bookings;
        } catch (Exception e) {
            System.err.println("Error fetching upcoming emergency bookings: " + e.getMessage());
            e.printStackTrace();
            // Fallback: get all bookings for this patient and filter manually
            return emergencyBookingRepository.findByPatientId(patientId).stream()
                    .filter(eb -> eb.getRequestedTime().isAfter(LocalDateTime.now()))
                    .filter(eb -> !"CANCELLED".equals(eb.getStatus()) && !"COMPLETED".equals(eb.getStatus()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get past emergency bookings for a patient
     */
    public List<EmergencyBooking> getPastEmergencyBookings(Long patientId) {
        try {
            List<EmergencyBooking> bookings = emergencyBookingRepository.findPastByPatientId(patientId, LocalDateTime.now());
            System.out.println("Found " + bookings.size() + " past emergency bookings for patient ID: " + patientId);
            return bookings;
        } catch (Exception e) {
            System.err.println("Error fetching past emergency bookings: " + e.getMessage());
            e.printStackTrace();
            // Fallback: get all bookings for this patient and filter manually
            return emergencyBookingRepository.findByPatientId(patientId).stream()
                    .filter(eb -> eb.getRequestedTime().isBefore(LocalDateTime.now()) ||
                            "COMPLETED".equals(eb.getStatus()) ||
                            "CANCELLED".equals(eb.getStatus()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Get all available doctors (approved doctors)
     */
    public List<Doctor> getAvailableDoctors() {
        return doctorService.getApprovedDoctors();
    }

    /**
     * Cancel an emergency booking
     */
    public EmergencyBooking cancelEmergencyBooking(Long bookingId) {
        Optional<EmergencyBooking> bookingOpt = emergencyBookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Emergency booking not found");
        }

        EmergencyBooking booking = bookingOpt.get();
        if ("CANCELLED".equals(booking.getStatus()) || "COMPLETED".equals(booking.getStatus())) {
            throw new IllegalStateException("Cannot cancel a booking that is already " + booking.getStatus());
        }

        booking.setStatus("CANCELLED");
        return emergencyBookingRepository.save(booking);
    }

    /**
     * Update emergency booking status
     */
    public EmergencyBooking updateStatus(Long bookingId, String status) {
        Optional<EmergencyBooking> bookingOpt = emergencyBookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Emergency booking not found");
        }

        EmergencyBooking booking = bookingOpt.get();
        booking.setStatus(status);
        return emergencyBookingRepository.save(booking);
    }

    /**
     * Get emergency booking by ID
     */
    public Optional<EmergencyBooking> getEmergencyBookingById(Long id) {
        return emergencyBookingRepository.findById(id);
    }

    /**
     * Get all emergency bookings by status
     */
    public List<EmergencyBooking> getEmergencyBookingsByStatus(String status) {
        return emergencyBookingRepository.findByStatus(status);
    }
}