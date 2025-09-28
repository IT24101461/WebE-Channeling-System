package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.repository.PatientRepository;
import com.webechannelingsystem.web_echannelingsystem.repository.AppointmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PatientDashboardController {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public PatientDashboardController(PatientRepository patientRepository,
                                      AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/patients/dashboard")
    public String patientDashboard(@RequestParam String email, Model model) {
        Optional<Patient> patient = patientRepository.findByEmail(email);

        if (!patient.isPresent()) {
            return "redirect:/patients/login";
        }

        // Get upcoming appointments (if you have appointments working)
        List<Appointment> upcomingAppointments;
        List<Appointment> pastAppointments;

        try {
            upcomingAppointments = appointmentRepository
                    .findByPatientIdAndAppointmentTimeAfterAndStatus(
                            patient.get().getId(),
                            LocalDateTime.now(),
                            "SCHEDULED"
                    );

            pastAppointments = appointmentRepository
                    .findByPatientIdAndAppointmentTimeBeforeOrStatus(
                            patient.get().getId(),
                            LocalDateTime.now(),
                            "COMPLETED"
                    );
        } catch (Exception e) {
            // If appointments don't work yet, use empty lists
            upcomingAppointments = List.of();
            pastAppointments = List.of();
        }

        model.addAttribute("patient", patient.get());
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);

        return "patient-dashboard";
    }
}