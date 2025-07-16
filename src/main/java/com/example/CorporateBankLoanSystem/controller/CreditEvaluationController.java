package com.example.CorporateBankLoanSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.CorporateBankLoanSystem.model.CreditEvaluation;
import com.example.CorporateBankLoanSystem.service.CreditEvaluationService;

@Controller
@RequestMapping("/credit")
public class CreditEvaluationController {
    @Autowired
    private CreditEvaluationService service;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("evaluation", new CreditEvaluation());
        return "credit-evaluation";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute CreditEvaluation evaluation) {
        service.evaluate(evaluation);
        return "redirect:/loan/list";
    }
}
