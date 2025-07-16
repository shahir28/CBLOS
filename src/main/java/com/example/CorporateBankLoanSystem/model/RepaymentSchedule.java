package com.example.CorporateBankLoanSystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedules")
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;

    @Column(nullable = false)
    private Integer installmentNumber;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private double principalAmount;

    @Column(nullable = false)
    private double interestAmount;

    @Column(nullable = false)
    private double totalPayment;

    @Column(nullable = false)
    private double remainingBalance;

    @Column(nullable = false)
    private String paymentStatus; // e.g., "PENDING", "PAID"

    // Constructors
    public RepaymentSchedule() {}

    public RepaymentSchedule(LoanApplication loanApplication, Integer installmentNumber, LocalDate dueDate,
                             double principalAmount, double interestAmount, double totalPayment,
                             double remainingBalance, String paymentStatus) {
        this.loanApplication = loanApplication;
        this.installmentNumber = installmentNumber;
        this.dueDate = dueDate;
        this.principalAmount = principalAmount;
        this.interestAmount = interestAmount;
        this.totalPayment = totalPayment;
        this.remainingBalance = remainingBalance;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "RepaymentSchedule{" +
               "id=" + id +
               ", loanApplicationId=" + (loanApplication != null ? loanApplication.getApplicationId() : "null") +
               ", installmentNumber=" + installmentNumber +
               ", dueDate=" + dueDate +
               ", principalAmount=" + principalAmount +
               ", interestAmount=" + interestAmount +
               ", totalPayment=" + totalPayment +
               ", remainingBalance=" + remainingBalance +
               ", paymentStatus='" + paymentStatus + '\'' +
               '}';
    }
}
