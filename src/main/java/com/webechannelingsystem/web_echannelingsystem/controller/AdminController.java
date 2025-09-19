package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Doctor;
import com.webechannelingsystem.web_echannelingsystem.service.AdminService;
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

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/pending-doctors")
    public List<Doctor> getPendingDoctors() {
        return adminService.getPendingDoctors();
    }

    @PostMapping("manage/approve/{id}")
    public String approveDoctor(@PathVariable Integer id) {
        adminService.approveDoctor(id);
        return "redirect:/admin/manage";
    }

    @PostMapping("manage/reject/{id}")
    public String rejectDoctor(@PathVariable Integer id) {
        adminService.rejectDoctor(id);
        return "redirect:/admin/manage";
    }

    @PostMapping("manage/delete/{id}")
    public String deleteDoctor(@PathVariable Integer id) {
        adminService.deleteDoctor(id);
        return "redirect:/admin/manage";
    }
}



