package com.webechannelingsystem.webechannelingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcomePage() {
        return "index";  // returns welcome.html from templates folder
    }
}
