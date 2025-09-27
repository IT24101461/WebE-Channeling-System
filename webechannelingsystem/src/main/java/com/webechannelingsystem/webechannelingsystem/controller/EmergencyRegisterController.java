package com.webechannelingsystem.webechannelingsystem.controller;

import com.webechannelingsystem.webechannelingsystem.model.Admin;
import com.webechannelingsystem.webechannelingsystem.model.Emergency;
import com.webechannelingsystem.webechannelingsystem.notification.EmailService;
import com.webechannelingsystem.webechannelingsystem.repository.AdminRepository;
import com.webechannelingsystem.webechannelingsystem.service.AdminService;
import com.webechannelingsystem.webechannelingsystem.service.EmergencyRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/emergency")
public class EmergencyRegisterController {

    @Autowired
    private EmergencyRegisterService emergencyRegisterService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminService adminService;

//    @Autowired
//    public AdminAuthController(AdminRepository adminRepository) {
//        this.adminRepository = adminRepository;
//    }

    // Show login/signup page
    @GetMapping("/register")
    public String loginPage() {
        return "EmergencyRegister"; // renders admin-login.html
    }

    // Handle login
    @PostMapping("/register")
    public String login(@RequestParam String patientName,
                        @RequestParam String patientEmail,
                        @RequestParam String patientContactNumber,
                        @RequestParam String patientAge,
                        @RequestParam String patientGender,
                        @RequestParam String patientIssues,
                        Model model) {


        Emergency emergency = new Emergency();
        emergency.setPatientFullName(patientName);
        emergency.setPatientEmail(patientEmail);
        emergency.setPatientContactNumber(patientContactNumber);
        emergency.setPatientAge(patientAge);
        emergency.setPatientGender(patientGender);
        emergency.setPatientIssues(patientIssues);
        emergency.setStatus("PENDING");

        final Emergency emergencySaved = emergencyRegisterService.save(emergency);

        // load all doctor emails from Doctor table
        final List<String> allDoctorEmails = adminService.findAllDoctorEmails();

        // Generate formatted email content
        String emailContent = generateEmailContent(emergencySaved);

        // Iterate over all emails and notify them
        allDoctorEmails.forEach(
              email ->  emailService.sendSimpleEmail(email, "New Emergency", emailContent)
        );

        return "redirect:/";
    }

    private String generateEmailContent(Emergency emergencySaved){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Dear Doctor, \n");
        stringBuilder.append("An emergency case has been logged as below. \n");
        stringBuilder.append("Patient Name: "+ emergencySaved.getPatientFullName() + "\n");
        stringBuilder.append("Patient Contact Number: "+ emergencySaved.getPatientContactNumber() + "\n");
        stringBuilder.append("Patient Email: "+ emergencySaved.getPatientEmail() + "\n");
        stringBuilder.append("Patient Age: "+ emergencySaved.getPatientAge() + "\n");
        stringBuilder.append("Patient Gender: "+ emergencySaved.getPatientGender() + "\n");
        stringBuilder.append("Patient Issues: "+ emergencySaved.getPatientIssues() + "\n");

        stringBuilder.append("Thank You !");
        return stringBuilder.toString();
    }
}
