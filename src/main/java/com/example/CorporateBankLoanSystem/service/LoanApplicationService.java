package com.example.CorporateBankLoanSystem.service;

import com.example.CorporateBankLoanSystem.model.LoanApplication;
import com.example.CorporateBankLoanSystem.model.User;
import com.example.CorporateBankLoanSystem.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    //Saves a new loan application or updates an existing one.
    public LoanApplication saveLoanApplication(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    //Retrieves a loan application by its ID.
    public Optional<LoanApplication> getLoanApplicationById(Long id) {
        return loanApplicationRepository.findById(id);
    }

    //Retrieves all loan applications.
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    //Retrieves all loan applications submitted by a specific user.
    public List<LoanApplication> getLoansByUser(User user) {
        return loanApplicationRepository.findByUser(user);
    }

    // --- New Methods for Admin Dashboard Counts ---

    //Gets the total number of all loan applications.
    public long getTotalApplications() {
        return loanApplicationRepository.count();
    }

    //Gets the number of loan applications with 'PENDING' status.
    public long getPendingApplicationsCount() {
        return loanApplicationRepository.countByStatus("PENDING");
    }

    // Gets the number of loan applications with 'APPROVED' status.
    public long getApprovedApplicationsCount() {
        return loanApplicationRepository.countByStatus("APPROVED");
    }

    //Gets the number of loan applications with 'REJECTED' status.
    public long getRejectedApplicationsCount() {
        return loanApplicationRepository.countByStatus("REJECTED");
    }

    // Deletes a loan application by its ID.
    public void deleteLoanApplication(Long id) {
        loanApplicationRepository.deleteById(id);
    }
}
