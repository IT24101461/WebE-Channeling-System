package com.webechannelingsystem.web_echannelingsystem.strategy;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RegularAppointmentStrategy implements AppointmentBookingStrategy {

    @Override
    public String validateBooking(Patient patient, Doctor doctor, LocalDateTime appointmentTime) {
        if (appointmentTime.isBefore(LocalDateTime.now().plusHours(24))) {
            return "Regular appointments must be booked at least 24 hours in advance";
        }
        // Add more validation logic as needed
        return null; // No error
    }

    @Override
    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime, String email) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setType("REGULAR");
        appointment.setStatus("SCHEDULED");
        appointment.setPaymentStatus("PENDING");
        appointment.setEmail(email);
        appointment.setPaymentMethod("NOT_SELECTED");
        return appointment;
    }

    @Override
    public String getConfirmationTemplate() {
        return "appointment-confirmation";
    }

    @Override
    public String handlePayment(Appointment appointment) {
        appointment.setPaymentStatus("PENDING");
        return "Payment required within 24 hours";
    }
}