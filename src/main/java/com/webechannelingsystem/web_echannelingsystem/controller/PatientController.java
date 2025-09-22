package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Patient;
import com.webechannelingsystem.web_echannelingsystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient) {
        patientService.registerPatient(patient);
        return "redirect:/success"; // Or wherever you want to redirect after registration
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
}

