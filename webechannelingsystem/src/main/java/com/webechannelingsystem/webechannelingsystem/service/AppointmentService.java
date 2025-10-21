package com.webechannelingsystem.webechannelingsystem.service;

import com.webechannelingsystem.webechannelingsystem.model.Appointment;
import com.webechannelingsystem.webechannelingsystem.repository.AppointmentRepository;
import com.webechannelingsystem.webechannelingsystem.model.Appointment;
import com.webechannelingsystem.webechannelingsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

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

    @Transactional
    public void deleteAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        appointmentRepository.deleteById(appointmentId);
    }

    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (!"SCHEDULED".equals(appointment.getStatus())) {
            throw new IllegalStateException("Only SCHEDULED appointments can be cancelled");
        }
        appointment.setStatus("CANCELLED");
        return appointmentRepository.save(appointment);
    }

    @Transactional
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