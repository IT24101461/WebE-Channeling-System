package com.webechannelingsystem.web_echannelingsystem.factory;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentFactory {

    public Appointment createBasicAppointment(Patient patient, Doctor doctor,
                                              LocalDateTime appointmentTime, String email) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setEmail(email);
        appointment.setPaymentMethod("NOT_SELECTED");
        return appointment;
    }

    public Appointment createAppointmentWithType(Patient patient, Doctor doctor,
                                                 LocalDateTime appointmentTime, String email,
                                                 String type, String status, String paymentStatus) {
        Appointment appointment = createBasicAppointment(patient, doctor, appointmentTime, email);
        appointment.setType(type);
        appointment.setStatus(status);
        appointment.setPaymentStatus(paymentStatus);
        return appointment;
    }

    public Appointment createScheduledAppointment(Patient patient, Doctor doctor,
                                                  LocalDateTime appointmentTime, String email) {
        return createAppointmentWithType(patient, doctor, appointmentTime, email,
                "REGULAR", "SCHEDULED", "PENDING");
    }

    public Appointment createEmergencyAppointment(Patient patient, Doctor doctor,
                                                  LocalDateTime appointmentTime, String email) {
        return createAppointmentWithType(patient, doctor, appointmentTime, email,
                "EMERGENCY", "PRIORITY", "REQUIRED_IMMEDIATELY");
    }
}