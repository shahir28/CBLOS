package com.example.CorporateBankLoanSystem.controller;

import com.example.CorporateBankLoanSystem.model.LoanApplication;
import com.example.CorporateBankLoanSystem.model.User;
import com.example.CorporateBankLoanSystem.service.LoanApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    // Method to display the loan application form
    @GetMapping("/loan-application/apply")
    public String showLoanApplicationForm(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            // If no user in session, redirect to login
            return "redirect:/login?error=true";
        }
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setUser(currentUser); // Associate the loan with the logged-in user
        model.addAttribute("loanApplication", loanApplication);
        return "loan-application-form"; // Refers to loan-application-form.html
    }



}
