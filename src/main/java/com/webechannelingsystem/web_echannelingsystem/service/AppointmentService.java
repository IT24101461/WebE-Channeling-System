package com.webechannelingsystem.web_echannelingsystem.service;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getUpcomingAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdAndAppointmentTimeAfterAndStatus(
                patientId, LocalDateTime.now(), "SCHEDULED");
    }

    public List<Appointment> getPastAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdAndAppointmentTimeBeforeOrStatus(
                patientId, LocalDateTime.now(), "COMPLETED");
    }

    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (!"SCHEDULED".equals(appointment.getStatus())) {
            throw new IllegalStateException("Only SCHEDULED appointments can be cancelled");
        }
        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }

    public Appointment rescheduleAppointment(Long appointmentId, LocalDateTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (!"SCHEDULED".equals(appointment.getStatus())) {
            throw new IllegalStateException("Only SCHEDULED appointments can be rescheduled");
        }
        appointment.setAppointmentTime(newTime);
        return appointmentRepository.save(appointment);
    }
}