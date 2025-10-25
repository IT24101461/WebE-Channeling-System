package com.webechannelingsystem.web_echannelingsystem.strategy;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmergencyAppointmentStrategy implements AppointmentBookingStrategy {

    @Override
    public String validateBooking(Patient patient, Doctor doctor, LocalDateTime appointmentTime) {
        if (appointmentTime.isAfter(LocalDateTime.now().plusHours(2))) {
            return "Emergency appointments must be within 2 hours";
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
        appointment.setType("EMERGENCY");
        appointment.setStatus("PRIORITY");
        appointment.setPaymentStatus("REQUIRED_IMMEDIATELY");
        appointment.setEmail(email);
        appointment.setPaymentMethod("NOT_SELECTED");
        return appointment;
    }

    @Override
    public String getConfirmationTemplate() {
        return "emergency-appointment-confirmation";
    }

    @Override
    public String handlePayment(Appointment appointment) {
        appointment.setPaymentStatus("REQUIRED_IMMEDIATELY");
        return "Immediate payment required";
    }
}