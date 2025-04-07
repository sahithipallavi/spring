package com.LoanRepayment.LoanRepayment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/") //shortcut for http get request in sb
    //it maps a specific url to method inside the controller
    public String index(@RequestParam(name = "applicationId", required = false) Long applicationId, Model model) {
        model.addAttribute("applicationId", applicationId);
        //adds the appid parameter to the model object
        return "index";
    }
}