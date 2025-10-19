package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Admin;
import com.webechannelingsystem.web_echannelingsystem.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final AdminRepository adminRepository;

    public AdminAuthController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Show login/signup page
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // renders admin-login.html
    }

    // Handle login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Admin admin = adminRepository.findByUsername(username).orElse(null);

        if (admin != null && admin.getPassword().equals(password)) {
            // Login successful, redirect to admin dashboard (pending doctors page)
            session.setAttribute("loggedInAdmin", admin);

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // clear session
        return "redirect:/admin/login?logout"; // back to login page
    }

}

