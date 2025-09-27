package com.webechannelingsystem.webechannelingsystem.service;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import com.webechannelingsystem.webechannelingsystem.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public AdminService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getPendingDoctors() {
        return doctorRepository.findByStatus("PENDING");
    }

    public Doctor approveDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        doctor.setStatus("APPROVED");
        return doctorRepository.save(doctor);
    }

    public void rejectDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        doctor.setStatus("REJECTED");
        doctorRepository.save(doctor);
    }


    public List<String> findAllDoctorEmails(){
        return doctorRepository.findAllDoctorEmails();
    }
}


