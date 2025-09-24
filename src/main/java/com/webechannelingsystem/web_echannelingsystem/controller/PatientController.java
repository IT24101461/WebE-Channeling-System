package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Existing registration endpoint
    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient, Model model) {
        patientService.registerPatient(patient);
        model.addAttribute("message", "Registration successful! Please log in.");
        return "patient-login"; // Redirect to patient login page
    }

    // New login endpoint
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<Patient> patient = patientService.getPatientByEmail(email);

        if (patient.isPresent() && patient.get().getPassword().equals(password)) {
            // Login successful, redirect to patient dashboard or home page
            return "redirect:/patients/dashboard"; // Adjust to your patient dashboard URL
        } else {
            // Login failed
            model.addAttribute("error", "Invalid email or password");
            return "patient-login"; // Return to login page with error
        }
    }

    // Existing endpoints
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

    // Optional: Add a patient dashboard endpoint
    @GetMapping("/dashboard")
    public String patientDashboard(Model model) {
        // Add logic to fetch patient-specific data if needed
        return "patient-dashboard"; // Create a patientDashboard.html template
    }
}