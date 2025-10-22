package com.webechannelingsystem.web_echannelingsystem.controller;

import com.webechannelingsystem.web_echannelingsystem.model.Admin;
import com.webechannelingsystem.web_echannelingsystem.repository.AdminRepository;
import com.webechannelingsystem.web_echannelingsystem.service.strategy.AuthenticationStrategy;
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
    private final AuthenticationStrategy authenticationStrategy; // ADDED

    // MODIFIED CONSTRUCTOR
    public AdminAuthController(AdminRepository adminRepository,
                               AuthenticationStrategy authenticationStrategy) {
        this.adminRepository = adminRepository;
        this.authenticationStrategy = authenticationStrategy;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Admin admin = adminRepository.findByUsername(username).orElse(null);

        // CHANGED: Use strategy instead of direct comparison
        if (admin != null && authenticationStrategy.authenticate(admin, password)) {
            session.setAttribute("loggedInAdmin", admin);
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String fullName,
                         @RequestParam String email,
                         @RequestParam String number,
                         @RequestParam String username,
                         @RequestParam String password,
                         Model model) {

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

        System.out.println("🚀 Signup request received for: ");
        adminRepository.save(admin);

        model.addAttribute("message", "Signup successful! You can now login.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?logout";
    }
}