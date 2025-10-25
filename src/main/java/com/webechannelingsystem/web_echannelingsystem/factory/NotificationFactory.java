package com.webechannelingsystem.web_echannelingsystem.factory;

import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

    public String createAppointmentConfirmationMessage(Patient patient, Appointment appointment) {
        String doctorName = "Doctor";
        if (appointment.getDoctor() != null) {
            doctorName = appointment.getDoctor().getFullName(); // CHANGED: getName() → getFullName()
        }

        return String.format(
                "Dear %s,\n\nYour appointment has been confirmed.\n" +
                        "Doctor: %s\nDate: %s\nTime: %s\nType: %s\n\nThank you!",
                patient.getFullName(),
                doctorName,
                appointment.getAppointmentTime().toLocalDate(),
                appointment.getAppointmentTime().toLocalTime(),
                appointment.getType()
        );
    }

    public String createEmergencyAlertMessage(Patient patient, Appointment appointment) {
        String doctorName = "Doctor";
        if (appointment.getDoctor() != null) {
            doctorName = appointment.getDoctor().getFullName(); // CHANGED: getName() → getFullName()
        }

        return String.format(
                "EMERGENCY ALERT!\n\nPatient: %s\nEmergency: %s\n" +
                        "Doctor: %s\nTime: %s\nPlease proceed immediately!",
                patient.getFullName(),
                "Medical Emergency",
                doctorName,
                appointment.getAppointmentTime().toString()
        );
    }

    public String createPaymentReminderMessage(Patient patient, Appointment appointment) {
        String doctorName = "Doctor";
        if (appointment.getDoctor() != null) {
            doctorName = appointment.getDoctor().getFullName(); // CHANGED: getName() → getFullName()
        }

        return String.format(
                "Payment Reminder for %s,\n\nYour appointment with Dr. %s on %s requires payment.\n" +
                        "Please complete payment to confirm your appointment.",
                patient.getFullName(),
                doctorName,
                appointment.getAppointmentTime().toString()
        );
    }
}