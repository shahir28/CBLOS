package com.example.CorporateBankLoanSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.CorporateBankLoanSystem.model.Approval;
import com.example.CorporateBankLoanSystem.service.ApprovalService;

@Controller
@RequestMapping("/approval")
public class ApprovalController {
    @Autowired
    private ApprovalService service;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("approval", new Approval());
        return "loan-approval";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute Approval approval) {
        service.approve(approval);
        return "redirect:/loan/list";
    }
}

