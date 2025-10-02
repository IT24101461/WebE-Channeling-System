package com.webechannelingsystem.web_echannelingsystem.service;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getUpcomingAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndAppointmentTimeAfterAndStatus(
                patientId, LocalDateTime.now(), "SCHEDULED");
        appointments.sort(Comparator.comparing(Appointment::getAppointmentTime));
        return appointments;
    }

    public List<Appointment> getPastAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndAppointmentTimeBeforeOrStatus(
                patientId, LocalDateTime.now(), "COMPLETED");
        appointments.sort(Comparator.comparing(Appointment::getAppointmentTime).reversed());
        return appointments;
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

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
}