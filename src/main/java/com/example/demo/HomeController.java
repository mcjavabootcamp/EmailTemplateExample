package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Autowired
    EmailService emailService;

    @RequestMapping("/")
    public String homePage(){
        return "homepage";
    }
    @RequestMapping("/sendemail")
    public String sendEmail(){
        emailService.SendSimpleEmail();
        return "confirmemail";
    }

}
