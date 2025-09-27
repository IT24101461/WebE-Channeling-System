package com.webechannelingsystem.webechannelingsystem.controller;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import com.webechannelingsystem.webechannelingsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private  AdminService adminService;

//    @Autowired
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }

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



