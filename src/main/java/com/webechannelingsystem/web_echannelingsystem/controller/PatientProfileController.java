package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.repository.PatientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PatientProfileController {

    private final PatientRepository patientRepository;

    public PatientProfileController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/patients/account/profile")
    public String patientProfile(@RequestParam String email, Model model) {
        Optional<Patient> patient = patientRepository.findByEmail(email);

        if (!patient.isPresent()) {
            return "redirect:/patients/login";
        }

        model.addAttribute("patient", patient.get());
        return "patient-profile";
    }
}