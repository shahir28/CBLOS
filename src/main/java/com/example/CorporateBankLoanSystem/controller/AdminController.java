package com.example.CorporateBankLoanSystem.controller;

import com.example.CorporateBankLoanSystem.model.LoanApplication;
import com.example.CorporateBankLoanSystem.model.User;
import com.example.CorporateBankLoanSystem.service.LoanApplicationService;
import com.example.CorporateBankLoanSystem.service.RepaymentService;
import com.example.CorporateBankLoanSystem.repository.UserRepository;
import com.example.CorporateBankLoanSystem.model.RepaymentSchedule;
import com.example.CorporateBankLoanSystem.service.DocumentService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepaymentService repaymentService;

    @Autowired(required = false)
    private DocumentService documentService;


    @GetMapping("/login")
    public String adminLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid admin credentials.");
        }
        return "admin-login";
    }

    @PostMapping("/login")
    public String adminLogin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if ("admin".equals(username) && "adminpass".equals(password)) {
            session.setAttribute("adminLoggedIn", true);
            System.out.println("Admin '" + username + "' logged in successfully.");
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password.");
            System.out.println("Failed admin login attempt for user '" + username + "'.");
            return "redirect:/admin/login?error=true";
        }
    }


    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        model.addAttribute("totalApplications", loanApplicationService.getTotalApplications());
        model.addAttribute("pendingApprovals", loanApplicationService.getPendingApplicationsCount());
        model.addAttribute("approvedLoans", loanApplicationService.getApprovedApplicationsCount());
        model.addAttribute("rejectedLoans", loanApplicationService.getRejectedApplicationsCount());
        return "admin-dashboard";
    }

    @GetMapping("/loans/all")
    public String viewAllLoans(HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        List<LoanApplication> loanList = loanApplicationService.getAllLoanApplications();
        model.addAttribute("loanList", loanList);
        return "admin-all-loans";
    }

    @GetMapping("/loans/details/{id}")
    public String viewLoanDetails(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        Optional<LoanApplication> loanOpt = loanApplicationService.getLoanApplicationById(id);
        if (loanOpt.isPresent()) {
            LoanApplication loan = loanOpt.get();
            model.addAttribute("loan", loan);
            return "admin-loan-details";
        } else {
            return "redirect:/admin/loans/all";
        }
    }

    @PostMapping("/loans/evaluate/{id}")
    public String evaluateLoan(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam(required = false) Double adminCreditScore,
                               @RequestParam(required = false) String adminEvaluationNotes,
                               @RequestParam(required = false) Double interestRate,
                               @RequestParam(required = false) Integer loanTermMonths,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }

        Optional<LoanApplication> loanOpt = loanApplicationService.getLoanApplicationById(id);
        if (loanOpt.isPresent()) {
            LoanApplication loan = loanOpt.get();
            loan.setStatus(status);
            loan.setAdminCreditScore(adminCreditScore);
            loan.setAdminEvaluationNotes(adminEvaluationNotes);
            loan.setApprovalRejectionDate(LocalDate.now());

            if ("APPROVED".equals(status) || "DISBURSED".equals(status)) {
                if (interestRate == null || loanTermMonths == null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Interest Rate and Loan Term are required for APPROVED/DISBURSED loans.");
                    return "redirect:/admin/loans/details/" + id;
                }

                loan.setInterestRate(interestRate);
                loan.setLoanTermMonths(loanTermMonths);

                try {
                    
                    if (loan.getInterestRate() != null && loan.getLoanTermMonths() != null) {
                        repaymentService.generateRepaymentSchedule(loan, loan.getInterestRate().doubleValue(), loan.getLoanTermMonths().intValue());
                        redirectAttributes.addFlashAttribute("successMessage", "Loan " + status + " and repayment schedule generated!");
                    } else {
                        redirectAttributes.addFlashAttribute("errorMessage", "Cannot generate repayment schedule: Interest Rate or Loan Term is null.");
                    }
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Loan " + status + " but failed to generate repayment schedule: " + e.getMessage());
                    System.err.println("Error generating repayment schedule: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                loan.setInterestRate(null);
                loan.setLoanTermMonths(null);
                redirectAttributes.addFlashAttribute("successMessage", "Loan status updated to " + status + ".");
            }

            loanApplicationService.saveLoanApplication(loan);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Loan application not found.");
        }
        return "redirect:/admin/loans/details/" + id;
    }

    @GetMapping("/users/manage")
    public String manageUsers(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-manage-users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "admin-edit-user";
        } else {
            return "redirect:/admin/users/manage";
        }
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user,
                             @RequestParam(required = false) String password,
                             RedirectAttributes redirectAttributes, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            if (password != null && !password.isEmpty()) {
                existingUser.setPassword(password);
            }
            userRepository.save(existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
        }
        return "redirect:/admin/users/manage";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/admin/users/manage";
    }

    @GetMapping("/loans/repayment-analysis/{applicationId}")
    public String viewAdminRepaymentAnalysis(@PathVariable Long applicationId, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login?error=true";
        }
        Optional<LoanApplication> loanOpt = loanApplicationService.getLoanApplicationById(applicationId);
        if (loanOpt.isPresent()) {
            LoanApplication loan = loanOpt.get();
            List<RepaymentSchedule> schedule = null;
            if ("APPROVED".equals(loan.getStatus()) || "DISBURSED".equals(loan.getStatus())) {
                schedule = repaymentService.getRepaymentScheduleByLoanApplicationId(applicationId);
            }

            model.addAttribute("loan", loan);
            model.addAttribute("repaymentSchedule", schedule);
            return "repayment-analysis";
        } else {
            model.addAttribute("errorMessage", "Loan application not found for repayment analysis.");
            return "redirect:/admin/loans/all";
        }
    }


    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("adminLoggedIn") != null && (Boolean) session.getAttribute("adminLoggedIn");
    }
}
