package com.webechannelingsystem.web_echannelingsystem.service;

import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import com.webechannelingsystem.web_echannelingsystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository; // REMOVE 'static' keyword

    public List<Doctor> getApprovedDoctors() { // REMOVE 'static' keyword
        return doctorRepository.findByStatus("APPROVED");
    }

    public List<String> getAllSpecializations() { // REMOVE 'static' keyword
        List<Doctor> doctors = getApprovedDoctors();
        return doctors.stream()
                .map(Doctor::getSpecialization)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return getApprovedDoctors().stream()
                .filter(doctor -> doctor.getSpecialization() != null &&
                        doctor.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }




}