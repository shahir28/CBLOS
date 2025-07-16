package com.example.CorporateBankLoanSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.CorporateBankLoanSystem.service.CibilService;

@Controller
@RequestMapping("/cibil")
public class CibilController {

    @Autowired
    private CibilService cibilService;

    // This method correctly handles the /cibil/calculator GET request
    @GetMapping("/calculator")
    public String showCibilCalculator(Model model) {
        return "cibil-calculator"; // Refers to cibil-calculator.html
    }
}
