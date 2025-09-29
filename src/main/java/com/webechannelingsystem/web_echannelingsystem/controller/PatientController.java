package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.service.AppointmentService;
import com.webechannelingsystem.web_echannelingsystem.service.PatientService;
import com.webechannelingsystem.web_echannelingsystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/auth/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        if (patientService.validatePatientCredentials(email, password)) {
            return "redirect:/patients/dashboard?email=" + email;
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "patient-login";
        }
    }

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient, Model model) {
        patientService.registerPatient(patient);
        model.addAttribute("message", "Registration successful! Please log in.");
        return "patient-login";
    }

    @GetMapping("/dashboard")
    public String patientDashboard(Model model, @RequestParam String email) {
        Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
        if (patientOpt.isEmpty()) {
            model.addAttribute("error", "Patient not found");
            return "patient-login";
        }
        Patient patient = patientOpt.get();
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments(patient.getId());
        List<Appointment> pastAppointments = appointmentService.getPastAppointments(patient.getId());
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        model.addAttribute("patient", patient);
        return "patient-dashboard";
    }

    @PostMapping("/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id, @RequestParam String email, Model model) {
        try {
            appointmentService.cancelAppointment(id);
            return "redirect:/patients/dashboard?email=" + email;
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return patientDashboard(model, email);
        }
    }

    @PostMapping("/appointments/reschedule/{id}")
    public String rescheduleAppointment(@PathVariable Long id, @RequestParam String newTime, @RequestParam String email, Model model) {
        try {
            LocalDateTime newDateTime = LocalDateTime.parse(newTime);
            appointmentService.rescheduleAppointment(id, newDateTime);
            return "redirect:/patients/dashboard?email=" + email;
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return patientDashboard(model, email);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<Patient> getPatient(@PathVariable String email) {
        return patientService.getPatientByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/account/profile")
    public String showProfile(@RequestParam String email, Model model) {
        Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
        if (patientOpt.isEmpty()) {
            model.addAttribute("error", "Patient not found");
            return "patient-dashboard";
        }
        model.addAttribute("patient", patientOpt.get());
        return "patient-profile";
    }

    @PostMapping("/account/profile/edit")
    public String editProfile(@ModelAttribute Patient patient, @RequestParam String email, Model model) {
        Optional<Patient> existingPatientOpt = patientService.getPatientByEmail(email);
        if (existingPatientOpt.isEmpty()) {
            model.addAttribute("error", "Patient not found");
            return "patient-profile";
        }
        Patient existingPatient = existingPatientOpt.get();
        existingPatient.setFullName(patient.getFullName());
        existingPatient.setContactNumber(patient.getContactNumber());
        existingPatient.setPassword(patient.getPassword());
        patientService.registerPatient(existingPatient); // Reuses save method
        return "redirect:/patients/dashboard?email=" + email;
    }

    @GetMapping("/book-appointment")
    public String showBookAppointmentForm(@RequestParam String email, Model model) {
        // Get patient
        Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
        if (patientOpt.isEmpty()) {
            return "redirect:/patients/login";
        }

        // Get all specializations and doctors - USE INSTANCE METHODS, NOT STATIC
        List<String> specializations = doctorService.getAllSpecializations(); // REMOVE 'DoctorService.' prefix
        List<Doctor> doctors = doctorService.getApprovedDoctors(); // REMOVE 'DoctorService.' prefix

        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("specializations", specializations);
        model.addAttribute("doctors", doctors);
        model.addAttribute("email", email);

        return "book-appointment";
    }

    @Autowired
    private DoctorService doctorService;


    @PostMapping("/appointments/book")
    public String bookAppointment(@RequestParam String patientEmail,
                                  @RequestParam Long doctorId,
                                  @RequestParam String appointmentTime,
                                  @RequestParam String type,
                                  Model model) {
        try {
            // Get patient
            Optional<Patient> patientOpt = patientService.getPatientByEmail(patientEmail);
            if (patientOpt.isEmpty()) {
                model.addAttribute("error", "Patient not found");
                return "redirect:/patients/login";
            }

            // Get doctor
            Doctor doctor = doctorService.getDoctorById(doctorId);
            if (doctor == null) {
                model.addAttribute("error", "Doctor not found");
                return "book-appointment";
            }

            // Create and save appointment
            Appointment appointment = new Appointment();
            appointment.setPatient(patientOpt.get());
            appointment.setDoctor(doctor);
            appointment.setAppointmentTime(LocalDateTime.parse(appointmentTime));
            appointment.setType(type);
            appointment.setStatus("SCHEDULED");
            appointment.setPaymentStatus("PENDING");
            appointment.setEmail(patientEmail);
            appointment.setPaymentMethod("NOT_SELECTED"); // Default

            appointmentService.saveAppointment(appointment);

            return "redirect:/patients/dashboard?email=" + patientEmail + "&success=Appointment booked successfully!";

        } catch (Exception e) {
            model.addAttribute("error", "Failed to book appointment: " + e.getMessage());
            return "book-appointment";
        }

    }



}

