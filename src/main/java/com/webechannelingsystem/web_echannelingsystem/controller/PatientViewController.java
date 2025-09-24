package com.webechannelingsystem.web_echannelingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientViewController {

    @GetMapping("/patient-auth-choice")
    public String showAuthChoicePage() {
        return "patient-auth-choice";
    }

    @GetMapping("/patients/register-page")
    public String showRegistrationForm() {
        return "patient-register";
    }

    @GetMapping("/patients/login")
    public String showLoginForm() {
        return "patient-login";
    }
}