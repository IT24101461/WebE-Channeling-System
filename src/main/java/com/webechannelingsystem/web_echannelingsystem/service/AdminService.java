package com.webechannelingsystem.web_echannelingsystem.service;

import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import com.webechannelingsystem.web_echannelingsystem.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final DoctorRepository doctorRepository;

    public AdminService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getPendingDoctors() {
        return doctorRepository.findByStatus("PENDING");
    }

    public Doctor approveDoctor(Integer id) {
        Doctor doctor = doctorRepository.findById(id.longValue()).orElseThrow();
        doctor.setStatus("APPROVED");
        return doctorRepository.save(doctor);
    }

    public void rejectDoctor(Integer id) {
        Doctor doctor = doctorRepository.findById(id.longValue()).orElseThrow();
        doctor.setStatus("REJECTED");
        doctorRepository.save(doctor);
    }

    public void deleteDoctor(Integer id) {
        Doctor doctor = doctorRepository.findById(id.longValue())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID " + id));
        doctorRepository.delete(doctor);
    }

    public Doctor getDoctorById(Integer id) {
        return doctorRepository.findById(id.longValue())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

}


