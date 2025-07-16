package com.example.CorporateBankLoanSystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal; // Ensure BigDecimal is imported

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String loanType;

    @Column(nullable = false)
    private BigDecimal loanAmount; 

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private String status; 

    // Fields for Admin Evaluation
    private Double adminCreditScore; 
    private String adminEvaluationNotes;
    private LocalDate approvalRejectionDate;

    // Fields for Approved Loan Terms (set by Admin)
    private Double interestRate; // Annual interest rate assigned by admin
    private Integer loanTermMonths; // Final loan term in months (derived from user's years input or directly set)

    // New fields for User's Loan Application Request
    private Integer requestedLoanTermYears; 
    private Integer cibilScore; 

    // One-to-Many relationship for Repayment Schedule
    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RepaymentSchedule> repaymentSchedules;

    // One-to-Many relationship for Documents (assuming Document model exists and is correctly mapped)
    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // EAGER fetch for Documents
    private List<Document> documents;

    // Constructors
    public LoanApplication() {
        this.submissionDate = LocalDate.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAdminCreditScore() {
        return adminCreditScore;
    }

    public void setAdminCreditScore(Double adminCreditScore) {
        this.adminCreditScore = adminCreditScore;
    }

    public String getAdminEvaluationNotes() {
        return adminEvaluationNotes;
    }

    public void setAdminEvaluationNotes(String adminEvaluationNotes) {
        this.adminEvaluationNotes = adminEvaluationNotes;
    }

    public LocalDate getApprovalRejectionDate() {
        return approvalRejectionDate;
    }

    public void setApprovalRejectionDate(LocalDate approvalRejectionDate) {
        this.approvalRejectionDate = approvalRejectionDate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getLoanTermMonths() {
        return loanTermMonths;
    }

    public void setLoanTermMonths(Integer loanTermMonths) {
        this.loanTermMonths = loanTermMonths;
    }

    public Integer getRequestedLoanTermYears() {
        return requestedLoanTermYears;
    }

    public void setRequestedLoanTermYears(Integer requestedLoanTermYears) {
        this.requestedLoanTermYears = requestedLoanTermYears;
    }

    public Integer getCibilScore() {
        return cibilScore;
    }

    public void setCibilScore(Integer cibilScore) {
        this.cibilScore = cibilScore;
    }

    public List<RepaymentSchedule> getRepaymentSchedules() {
        return repaymentSchedules;
    }

    public void setRepaymentSchedules(List<RepaymentSchedule> repaymentSchedules) {
        this.repaymentSchedules = repaymentSchedules;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
               "applicationId=" + applicationId +
               ", userId=" + (user != null ? user.getId() : "null") +
               ", companyName='" + companyName + '\'' +
               ", loanType='" + loanType + '\'' +
               ", loanAmount=" + loanAmount +
               ", submissionDate=" + submissionDate +
               ", status='" + status + '\'' +
               ", adminCreditScore=" + adminCreditScore +
               ", adminEvaluationNotes='" + adminEvaluationNotes + '\'' +
               ", approvalRejectionDate=" + approvalRejectionDate +
               ", interestRate=" + interestRate +
               ", loanTermMonths=" + loanTermMonths +
               ", requestedLoanTermYears=" + requestedLoanTermYears +
               ", cibilScore=" + cibilScore +
               '}';
    }
}
