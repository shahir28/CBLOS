package com.example.CorporateBankLoanSystem.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.CorporateBankLoanSystem.model.LoanApplication;
import com.example.CorporateBankLoanSystem.model.User;
import com.example.CorporateBankLoanSystem.model.RepaymentSchedule;
import com.example.CorporateBankLoanSystem.repository.UserRepository;
import com.example.CorporateBankLoanSystem.service.LoanApplicationService;
import com.example.CorporateBankLoanSystem.service.RepaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanApplicationService loanapplicationService;

    @Autowired
    private RepaymentService repaymentService;


    @GetMapping("/")
    public String welcomePage() {
        return "welcome";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    // --- User Authentication and Dashboard ---

    @GetMapping("/login")
    public String showLoginForm(Model model,
                                 @RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "registered", required = false) String registered) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Signup successful! Please log in.");
        }
        return "login";
    }

    // Process user login form (simulated backend check)
    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model,
                            HttpSession session) {
        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user != null) {
            session.setAttribute("user", user);
            System.out.println("User '" + username + "' logged in successfully (simulated).");
            return "redirect:/user/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            System.out.println("Failed login attempt for user '" + username + "'.");
            return "login";
        }
    }

    // Show signup form
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    // Process user signup
    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String fullName,
                               @RequestParam String email,
                               RedirectAttributes redirectAttributes) {
        User newUser = new User(username, password, fullName, email);
        userRepository.save(newUser);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login?registered=true";
    }


    // Show user dashboard
    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login?error=true";
        }
        model.addAttribute("displayName", user.getFullName() != null ? user.getFullName() : user.getUsername());
        return "user-dashboard";
    }

    // --- Financial Tips Page ---
    @GetMapping("/user/financial-tips")
    public String financialTipsPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login?error=true";
        }
        return "financial-tips";
    }

    // Show Loan Application Form
    @GetMapping("/loan/submit")
    public String showLoanApplicationForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login?error=true";
        }
        model.addAttribute("loanApplication", new LoanApplication());
        return "loan-application-form"; 
    }

    // Process Loan Application Submission 
    @PostMapping("/loan/submit")
    public String submitLoanApplication(@ModelAttribute LoanApplication loanApplication,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login?error=true";
        }
        loanApplication.setUser(user);
        loanApplication.setSubmissionDate(LocalDate.now());
        loanApplication.setStatus("PENDING");

        if (loanApplication.getRequestedLoanTermYears() != null) {
            loanApplication.setLoanTermMonths(loanApplication.getRequestedLoanTermYears() * 12);
        } else {
            loanApplication.setLoanTermMonths(null);
        }

        loanapplicationService.saveLoanApplication(loanApplication);
        redirectAttributes.addFlashAttribute("successMessage", "Loan application submitted successfully! Application ID: " + loanApplication.getApplicationId());
        
        return "redirect:/user/documents/upload?applicationId=" + loanApplication.getApplicationId();
    }




    // View Loan Status
    @GetMapping("/loan/status")
    public String viewLoanStatus(@RequestParam(name = "applicationId", required = false) Long applicationId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login?error=true";
        }

        if (applicationId != null) {
            Optional<LoanApplication> loanOpt = loanapplicationService.getLoanApplicationById(applicationId);
            if (loanOpt.isPresent()) {
                if (!loanOpt.get().getUser().getId().equals(user.getId())) {
                    model.addAttribute("error", "You do not have permission to view this loan.");
                    return "loan-status";
                }
                model.addAttribute("loan", loanOpt.get());
            } else {
                model.addAttribute("error", "Loan application not found for ID: " + applicationId);
            }
            return "loan-status";
        } else {
            List<LoanApplication> userLoans = loanapplicationService.getLoansByUser(user);
            model.addAttribute("loanList", userLoans);
            return "loan-status";
        }
    }

    // User: View Repayment Analysis
    @GetMapping("/user/loan/repayment-analysis/{applicationId}")
    public String viewUserRepaymentAnalysis(@PathVariable Long applicationId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login?error=true";
        }
        Optional<LoanApplication> loanOpt = loanapplicationService.getLoanApplicationById(applicationId);
        if (loanOpt.isPresent()) {
            LoanApplication loan = loanOpt.get();
            if (!loan.getUser().getId().equals(user.getId())) {
                model.addAttribute("errorMessage", "You do not have permission to view this repayment schedule.");
                return "redirect:/user/dashboard";
            }

            List<RepaymentSchedule> schedule = null;
            if ("APPROVED".equals(loan.getStatus()) || "DISBURSED".equals(loan.getStatus())) {
                schedule = repaymentService.getRepaymentScheduleByLoanApplicationId(applicationId);
            }

            model.addAttribute("loan", loan);
            model.addAttribute("repaymentSchedule", schedule);
            return "repayment-analysis";
        } else {
            model.addAttribute("errorMessage", "Loan application not found for repayment analysis.");
            return "redirect:/user/dashboard";
        }
    }
    @GetMapping("/privacy")
    public String showprivacypolicy() {
    	return "privacy";
    }


    // --- Forgot Password ---

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        System.out.println("Simulating password reset request for email: " + email);
        return "redirect:/forgot-password-success";
    }

    @GetMapping("/forgot-password-success")
    public String showForgotPasswordSuccess() {
        return "forgot-password-success";
    }


    // --- Logout ---

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("User logged out. Session invalidated.");
        return "redirect:/";
    }
}
