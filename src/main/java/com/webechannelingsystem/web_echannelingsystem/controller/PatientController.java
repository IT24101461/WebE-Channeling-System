package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.service.AppointmentService;
import com.webechannelingsystem.web_echannelingsystem.service.DoctorService;
import com.webechannelingsystem.web_echannelingsystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/auth/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        if (patientService.validatePatientCredentials(email, password)) {
            session.setAttribute("loggedInPatientEmail", email); // Store email in session
            return "redirect:/patients/dashboard";
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

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-register"; // Assumes a patient-register.html template exists
    }

    @GetMapping("/dashboard")
    public String patientDashboard(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
        if (patientOpt.isEmpty()) {
            model.addAttribute("error", "Patient not found");
            session.invalidate();
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
    public String cancelAppointment(@PathVariable Long id, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        try {
            appointmentService.deleteAppointment(id);
            return "redirect:/patients/dashboard?success=Appointment cancelled successfully!";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return patientDashboard(model, session);
        }
    }

    @GetMapping("/reschedule-appointment/{id}")
    public String showReschedulePage(@PathVariable Long id, Model model, HttpSession session) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        Optional<Appointment> appointmentOpt = appointmentService.getAppointmentById(id);
        if (appointmentOpt.isEmpty()) {
            model.addAttribute("error", "Appointment not found");
            return "redirect:/patients/dashboard";
        }
        model.addAttribute("appointment", appointmentOpt.get());
        return "reschedule-appointment";
    }

    @PostMapping("/appointments/reschedule/{id}")
    public String rescheduleAppointment(@PathVariable Long id, @RequestParam String newTime, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        try {
            LocalDateTime newDateTime = LocalDateTime.parse(newTime);
            appointmentService.rescheduleAppointment(id, newDateTime);
            return "redirect:/patients/dashboard?success=Appointment rescheduled successfully!";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "reschedule-appointment";
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
    public String showProfile(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
        if (patientOpt.isEmpty()) {
            model.addAttribute("error", "Patient not found");
            return "patient-dashboard";
        }
        model.addAttribute("patient", patientOpt.get());
        return "patient-profile";
    }

    @PostMapping("/account/profile/edit")
    public String editProfile(@ModelAttribute Patient patient, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        Optional<Patient> existingPatientOpt = patientService.getPatientByEmail(email);
        if (existingPatientOpt.isEmpty()) {
            model.addAttribute("error", "Patient not found");
            return "patient-profile";
        }
        Patient existingPatient = existingPatientOpt.get();
        existingPatient.setFullName(patient.getFullName());
        existingPatient.setContactNumber(patient.getContactNumber());
        existingPatient.setPassword(patient.getPassword());
        patientService.registerPatient(existingPatient);
        return "redirect:/patients/dashboard";
    }

    @GetMapping("/book-appointment")
    public String showBookAppointmentForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
        if (patientOpt.isEmpty()) {
            return "redirect:/patients/login";
        }

        List<String> specializations = doctorService.getAllSpecializations();
        List<Doctor> doctors = doctorService.getApprovedDoctors();

        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("specializations", specializations);
        model.addAttribute("doctors", doctors);
        model.addAttribute("email", email);

        return "book-appointment";
    }

    @PostMapping("/appointments/book")
    public String bookAppointment(@RequestParam String patientEmail,
                                  @RequestParam Long doctorId,
                                  @RequestParam String appointmentTime,
                                  @RequestParam String type,
                                  HttpSession session,
                                  Model model) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        try {
            Optional<Patient> patientOpt = patientService.getPatientByEmail(patientEmail);
            if (patientOpt.isEmpty()) {
                model.addAttribute("error", "Patient not found");
                return "redirect:/patients/login";
            }

            Doctor doctor = doctorService.getDoctorById(doctorId);
            if (doctor == null) {
                model.addAttribute("error", "Doctor not found");
                return "book-appointment";
            }

            Appointment appointment = new Appointment();
            appointment.setPatient(patientOpt.get());
            appointment.setDoctor(doctor);
            appointment.setAppointmentTime(LocalDateTime.parse(appointmentTime));
            appointment.setType(type);
            appointment.setStatus("SCHEDULED");
            appointment.setPaymentStatus("PENDING");
            appointment.setEmail(patientEmail);
            appointment.setPaymentMethod("NOT_SELECTED");

            appointmentService.saveAppointment(appointment);

            return "redirect:/patients/dashboard?success=Appointment booked successfully!";

        } catch (Exception e) {
            model.addAttribute("error", "Failed to book appointment: " + e.getMessage());
            return "book-appointment";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/patients/login"; // Redirect to login page
    }

    @PostMapping("/account/delete")
    public String deleteAccount(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInPatientEmail");
        if (email == null) {
            return "redirect:/patients/login";
        }
        try {
            patientService.deletePatient(email);
            session.invalidate();
            model.addAttribute("message", "Account deleted successfully. Goodbye!");
            return "patient-login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "patient-profile";
        }
    }
}