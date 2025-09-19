package com.webechannelingsystem.webechannelingsystem.controller;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import com.webechannelingsystem.webechannelingsystem.service.AdminService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/pending-doctors")
    public List<Doctor> getPendingDoctors() {
        return adminService.getPendingDoctors();
    }

    @PostMapping("/approve/{id}")
    public Doctor approveDoctor(@PathVariable Long id) {
        return adminService.approveDoctor(id);
    }

    @DeleteMapping("/reject/{id}")
    public void rejectDoctor(@PathVariable Long id) {
        adminService.rejectDoctor(id);
    }
}



