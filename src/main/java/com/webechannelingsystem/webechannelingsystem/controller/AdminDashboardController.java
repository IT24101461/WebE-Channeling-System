package com.webechannelingsystem.webechannelingsystem.controller;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import com.webechannelingsystem.webechannelingsystem.repository.DoctorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminDashboardController {

    private final DoctorRepository doctorRepository;

    public AdminDashboardController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Dashboard showing counts
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        long pendingCount = doctorRepository.countByStatus("PENDING");
        long approvedCount = doctorRepository.countByStatus("APPROVED");
        long rejectedCount = doctorRepository.countByStatus("REJECTED");

        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("rejectedCount", rejectedCount);

        return "adminDashboard"; // adminDashboard.html
    }

    // Manage page showing all doctors lists
    @GetMapping("/admin/manage")
    public String manageDoctors(Model model) {
        List<Doctor> pendingDoctors = doctorRepository.findByStatus("PENDING");
        List<Doctor> approvedDoctors = doctorRepository.findByStatus("APPROVED");
        List<Doctor> rejectedDoctors = doctorRepository.findByStatus("REJECTED");

        model.addAttribute("pendingDoctors", pendingDoctors);
        model.addAttribute("approvedDoctors", approvedDoctors);
        model.addAttribute("rejectedDoctors", rejectedDoctors);

        return "admin-manage"; // adminManage.html
    }
}
