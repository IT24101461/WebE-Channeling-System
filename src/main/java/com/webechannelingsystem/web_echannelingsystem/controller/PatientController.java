package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Appointment;
import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.model.EmergencyBooking;
import com.webechannelingsystem.web_echannelingsystem.service.AppointmentService;
import com.webechannelingsystem.web_echannelingsystem.service.PatientService;
import com.webechannelingsystem.web_echannelingsystem.service.DoctorService;
import com.webechannelingsystem.web_echannelingsystem.service.EmergencyBookingService;
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

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EmergencyBookingService emergencyBookingService;

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

        // Get regular appointments
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments(patient.getId());
        List<Appointment> pastAppointments = appointmentService.getPastAppointments(patient.getId());

        // Get emergency bookings
        List<EmergencyBooking> upcomingEmergencyBookings = emergencyBookingService.getUpcomingEmergencyBookings(patient.getId());
        List<EmergencyBooking> pastEmergencyBookings = emergencyBookingService.getPastEmergencyBookings(patient.getId());

        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        model.addAttribute("upcomingEmergencyBookings", upcomingEmergencyBookings);
        model.addAttribute("pastEmergencyBookings", pastEmergencyBookings);
        model.addAttribute("patient", patient);
        return "patient-dashboard";
    }

    // Permanently deletes appointment
    @PostMapping("/appointments/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id, @RequestParam String email, Model model) {
        try {
            appointmentService.deleteAppointment(id);
            return "redirect:/patients/dashboard?email=" + email + "&success=Appointment deleted successfully!";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return patientDashboard(model, email);
        }
    }

    // NEW: Show reschedule form page
    @GetMapping("/appointments/reschedule/{id}")
    public String showRescheduleForm(@PathVariable Long id,
                                     @RequestParam String email,
                                     Model model) {
        try {
            Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
            if (patientOpt.isEmpty()) {
                return "redirect:/patients/login";
            }

            Optional<Appointment> appointmentOpt = appointmentService.getAppointmentById(id);
            if (appointmentOpt.isEmpty()) {
                model.addAttribute("error", "Appointment not found");
                return "redirect:/patients/dashboard?email=" + email;
            }

            Appointment appointment = appointmentOpt.get();

            // Check if appointment can be rescheduled
            if (!"SCHEDULED".equals(appointment.getStatus())) {
                model.addAttribute("error", "Only SCHEDULED appointments can be rescheduled");
                return "redirect:/patients/dashboard?email=" + email;
            }

            model.addAttribute("appointment", appointment);
            model.addAttribute("patient", patientOpt.get());
            return "reschedule-appointment";

        } catch (Exception e) {
            model.addAttribute("error", "Error loading reschedule form: " + e.getMessage());
            return "redirect:/patients/dashboard?email=" + email;
        }
    }

    // Process reschedule submission
    @PostMapping("/appointments/reschedule/{id}")
    public String rescheduleAppointment(@PathVariable Long id,
                                        @RequestParam String newTime,
                                        @RequestParam String email,
                                        Model model) {
        try {
            LocalDateTime newDateTime = LocalDateTime.parse(newTime);
            appointmentService.rescheduleAppointment(id, newDateTime);
            return "redirect:/patients/dashboard?email=" + email + "&success=Appointment rescheduled successfully!";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/patients/dashboard?email=" + email;
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
        patientService.registerPatient(existingPatient);
        return "redirect:/patients/dashboard?email=" + email;
    }

    @GetMapping("/book-appointment")
    public String showBookAppointmentForm(@RequestParam String email, Model model) {
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
                                  Model model) {
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

            Appointment savedAppointment = appointmentService.saveAppointment(appointment);

            model.addAttribute("appointment", savedAppointment);
            model.addAttribute("patient", patientOpt.get());
            return "appointment-confirmation";

        } catch (Exception e) {
            model.addAttribute("error", "Failed to book appointment: " + e.getMessage());
            return "book-appointment";
        }
    }

    @GetMapping("/payment/process")
    public String processPayment(@RequestParam Long appointmentId,
                                 @RequestParam String email,
                                 Model model) {
        try {
            Optional<Appointment> appointmentOpt = appointmentService.getAppointmentById(appointmentId);
            if (appointmentOpt.isPresent()) {
                Appointment appointment = appointmentOpt.get();
                appointment.setPaymentStatus("PAID");
                appointmentService.saveAppointment(appointment);
                return "redirect:/patients/dashboard?email=" + email + "&success=Payment completed successfully!";
            } else {
                return "redirect:/patients/dashboard?email=" + email + "&error=Appointment not found";
            }
        } catch (Exception e) {
            return "redirect:/patients/dashboard?email=" + email + "&error=Payment failed: " + e.getMessage();
        }
    }

    // ========== EMERGENCY BOOKING ENDPOINTS ==========

    @GetMapping("/emergency-booking")
    public String showEmergencyBookingForm(@RequestParam String email, Model model) {
        Optional<Patient> patientOpt = patientService.getPatientByEmail(email);
        if (patientOpt.isEmpty()) {
            return "redirect:/patients/login";
        }

        List<Doctor> availableDoctors = emergencyBookingService.getAvailableDoctors();

        model.addAttribute("patient", patientOpt.get());
        model.addAttribute("doctors", availableDoctors);
        model.addAttribute("email", email);

        return "emergency-booking";
    }

    @PostMapping("/emergency-booking/book")
    public String bookEmergencyAppointment(@RequestParam String patientEmail,
                                           @RequestParam Long doctorId,
                                           @RequestParam String requestedTime,
                                           @RequestParam(required = false) String emergencyType,
                                           @RequestParam(required = false) String notes,
                                           Model model) {
        try {
            Optional<Patient> patientOpt = patientService.getPatientByEmail(patientEmail);
            if (patientOpt.isEmpty()) {
                model.addAttribute("error", "Patient not found");
                return "redirect:/patients/login";
            }

            Doctor doctor = doctorService.getDoctorById(doctorId);
            if (doctor == null) {
                model.addAttribute("error", "Doctor not found");
                return showEmergencyBookingForm(patientEmail, model);
            }

            EmergencyBooking emergencyBooking = new EmergencyBooking();
            emergencyBooking.setPatient(patientOpt.get());
            emergencyBooking.setDoctor(doctor);
            emergencyBooking.setRequestedTime(LocalDateTime.parse(requestedTime));
            emergencyBooking.setPatientEmail(patientEmail);
            emergencyBooking.setEmergencyType(emergencyType != null ? emergencyType : "General Emergency");
            emergencyBooking.setNotes(notes);
            emergencyBooking.setStatus("PENDING");
            emergencyBooking.setPriority("HIGH");
            emergencyBooking.setPaymentStatus("PENDING");
            emergencyBooking.setPaymentMethod("NOT_SELECTED");

            emergencyBookingService.saveEmergencyBooking(emergencyBooking);

            return "redirect:/patients/dashboard?email=" + patientEmail + "&success=Emergency booking requested successfully!";

        } catch (Exception e) {
            model.addAttribute("error", "Failed to book emergency appointment: " + e.getMessage());
            return showEmergencyBookingForm(patientEmail, model);
        }
    }

    @PostMapping("/emergency-booking/cancel/{id}")
    public String cancelEmergencyBooking(@PathVariable Long id, @RequestParam String email) {
        try {
            emergencyBookingService.cancelEmergencyBooking(id);
            return "redirect:/patients/dashboard?email=" + email + "&success=Emergency booking cancelled";
        } catch (Exception e) {
            return "redirect:/patients/dashboard?email=" + email + "&error=" + e.getMessage();
        }
    }
}