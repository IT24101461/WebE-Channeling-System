package com.webechannelingsystem.web_echannelingsystem.strategy;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.model.Doctor;

import java.time.LocalDateTime;

public interface AppointmentBookingStrategy {
    String validateBooking(Patient patient, Doctor doctor, LocalDateTime appointmentTime);
    Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime, String email);
    String getConfirmationTemplate();
    String handlePayment(Appointment appointment);
}