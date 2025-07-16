package com.example.CorporateBankLoanSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.CorporateBankLoanSystem.model.Disbursement;
import com.example.CorporateBankLoanSystem.service.DisbursementService;

@Controller
@RequestMapping("/disbursement")
public class DisbursementController {
    @Autowired
    private DisbursementService service;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("disbursement", new Disbursement());
        return "disbursement-track";
    }

    @PostMapping("/track")
    public String submit(@ModelAttribute Disbursement disbursement) {
        service.track(disbursement);
        return "redirect:/loan/list";
    }
}