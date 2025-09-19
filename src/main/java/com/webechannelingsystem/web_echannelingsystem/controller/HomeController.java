package com.webechannelingsystem.web_echannelingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcomePage() {
        return "index";  // returns welcome.html from templates folder
    }
}
