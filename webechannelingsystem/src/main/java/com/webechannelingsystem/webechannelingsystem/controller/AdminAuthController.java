package com.webechannelingsystem.webechannelingsystem.controller;

import com.webechannelingsystem.webechannelingsystem.model.Admin;
import com.webechannelingsystem.webechannelingsystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AdminRepository adminRepository;

//    @Autowired
//    public AdminAuthController(AdminRepository adminRepository) {
//        this.adminRepository = adminRepository;
//    }

    // Show login/signup page
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // renders admin-login.html
    }

    // Handle login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        Admin admin = adminRepository.findByUsername(username).orElse(null);

        if (admin != null && admin.getPassword().equals(password)) {
            // Login successful, redirect to admin dashboard (pending doctors page)
            return "redirect:/admin/dashboard";
        } else {
            // Login failed
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Handle signup
    @PostMapping("/signup")
    public String signup(@RequestParam String fullName,
                         @RequestParam String email,
                         @RequestParam String number,
                         @RequestParam String username,
                         @RequestParam String password,
                         Model model) {

        // Check if username already exists
        if (adminRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "login";
        }

        Admin admin = new Admin();
        admin.setFullName(fullName);
        admin.setEmail(email);
        admin.setNumber(number);
        admin.setUsername(username);
        admin.setPassword(password);

        adminRepository.save(admin);

        model.addAttribute("message", "Signup successful! You can now login.");
        return "login"; // redirect to login form
    }
}
